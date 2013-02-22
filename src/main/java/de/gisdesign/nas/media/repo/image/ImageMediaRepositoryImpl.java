package de.gisdesign.nas.media.repo.image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import de.gisdesign.nas.media.admin.ConfigurationService;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.domain.image.ColorSpace;
import de.gisdesign.nas.media.domain.image.FlashMode;
import de.gisdesign.nas.media.domain.image.ImageCameraData;
import de.gisdesign.nas.media.domain.image.ImageCatalogEntry;
import de.gisdesign.nas.media.domain.image.ImageExposureData;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.domain.image.ImageMetaData;
import de.gisdesign.nas.media.domain.image.ImageSourceData;
import de.gisdesign.nas.media.domain.image.WhiteBalanceMode;
import de.gisdesign.nas.media.repo.MediaFileLibraryManager;
import de.gisdesign.nas.media.repo.MediaFileScanException;
import static de.gisdesign.nas.media.repo.image.Configuration.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Denis Pasek
 */
@Component
public class ImageMediaRepositoryImpl implements ImageMediaRepository {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImageMediaRepositoryImpl.class);

    @Autowired
    private ConfigurationService configService;

    @Autowired
    private MediaFileLibraryManager mediaFileLibraryManager;

    @Autowired
    private ImageRepositoryDAO imageRepositoryDAO;

    @Autowired
    private ThumbGenerator thumbGenerator;

    @Override
    public List<String> getMediaFileLibraryNames() {
        return this.mediaFileLibraryManager.getMediaFileLibraryNames(MediaFileType.IMAGE);
    }

    @Override
    public MediaFileLibrary getMediaFileLibrary(String libraryName) {
        return this.mediaFileLibraryManager.getMediaFileLibrary(MediaFileType.IMAGE, libraryName);
    }

    @Override
    public MediaFileType getSupportedMediaFileType() {
        return MediaFileType.IMAGE;
    }

    @Override
    public CatalogEntry createMediaFileCatalogEntry(CatalogEntry parent, ImageFileData mediaFileData) {
        Validate.notNull(parent, "Parent CatalogEntry is null.");
        Validate.notNull(mediaFileData, "MediaFileData is null.");
        Validate.isTrue(ImageFileData.class.isAssignableFrom(mediaFileData.getClass()), "MediaFileData is not of type ImageFileData.");
        ImageFileData imageData = mediaFileData;
        File imageFile = new File(imageData.getAbsolutePath(), imageData.getFilename());
        return imageFile.exists() ? new ImageCatalogEntry(parent, imageData) : null;
    }

    @Override
    public ImageFileData loadMediaFileData(Long id) {
        Validate.notNull(id, "ID is null.");
        return imageRepositoryDAO.findImageById(id);
    }

    @Override
    public ImageFileData loadMediaFileData(File imageFile) {
        validateMediaFile(imageFile);
        return imageRepositoryDAO.findImageByAbsoluteFileName(imageFile.getAbsolutePath());
    }

    @Transactional
    @Override
    public ImageFileData createMediaFileData(File imageFile) throws MediaFileScanException {
        validateMediaFile(imageFile);
        ImageFileData imageData;
        if (isSupportedMediaFile(imageFile)) {
            imageData = new ImageFileData();
            imageData.setAbsolutePath(imageFile.getParent());
            imageData.setFilename(imageFile.getName());
            imageData.setLastModified(imageFile.lastModified());
            imageData.setSize(imageFile.length());
            scanImageFileMetaData(imageFile, imageData);
            imageData = imageRepositoryDAO.saveImage(imageData);
        } else {
            throw new MediaFileScanException("Unsupported Image media file [" + imageFile.getAbsolutePath() + "]");
        }
        return imageData;
    }

    @Transactional
    @Override
    public ImageFileData updateMediaFileData(ImageFileData mediaFileData) throws MediaFileScanException {
        File imageFile = new File(mediaFileData.getAbsolutePath(), mediaFileData.getFilename());
        //Only rescan meta data if timestamp has changed.
        if (mediaFileData.hasChanged(imageFile.lastModified())) {
            scanImageFileMetaData(imageFile, mediaFileData);
            mediaFileData.setLastModified(imageFile.lastModified());
            mediaFileData.setSize(imageFile.length());
        }
        //Update sync ID and store metadata.
        return imageRepositoryDAO.saveImage(mediaFileData);
    }

    @Transactional
    @Override
    public void deleteMediaFileData(ImageFileData mediaFileData) {
        imageRepositoryDAO.deleteImage(mediaFileData);
    }

    @Override
    public Map<String,ImageFileData> loadMediaFilesFromDirectory(File directory) {
        validateMediaFileDirectory(directory);
        Map<String,ImageFileData> mediaFileDataMap = new HashMap<String, ImageFileData>();
        List<ImageFileData> mediaFileDataList = imageRepositoryDAO.findImagesByDirectory(directory.getAbsolutePath());
        LOG.debug("Loaded {} MediaFileData for media files in directory [{}].", mediaFileDataList.size(), directory.getAbsolutePath());
        for (ImageFileData mediaFileData : mediaFileDataList) {
            mediaFileDataMap.put(mediaFileData.getFilename(), mediaFileData);
        }
        return mediaFileDataMap;
    }

    @Override
    public boolean isSupportedMediaFile(File imageFile) {
        validateMediaFile(imageFile);
        //TODO: Extend check for supported image files.
        return imageFile.getName().toLowerCase().endsWith("jpg");
    }

    @Override
    public List<ImageFileData> findMediaFilesByCriteria(MetaDataCriteria<?> criteria) {
        List<ImageFileData> imageFiles = imageRepositoryDAO.findImagesByCriteria(criteria);
        LOG.debug("Loaded [{}] Image files for MetaDataCriteria [{}]", imageFiles.size(), criteria.dumpHierarchy());
        return imageFiles;
    }

    @Override
    public <T> List<T> loadMetaDataCriteriaOptions(MetaDataCriteria<T> metaDataCriteria) {
        List<T> criteriaValues = imageRepositoryDAO.loadImageCriteriaValues(metaDataCriteria);
        LOG.debug("Loaded MetaDataCriteriaValues {} for MetaDataCriteria [{}]", criteriaValues, metaDataCriteria.dumpHierarchy());
        return criteriaValues;
    }

    @Override
    public ScaledImageResources getScaledImageResources(ImageFileData imageFileData) {
        Validate.notNull(imageFileData, "ImageFileData is null.");
        File outputDirectory = determineOutputDirectory();

        //Check slide show image file.
        Integer slideShowHeight = determineSlideShowImageHeight();
        File slideShowFile = imageFileData.createSlideShowFileForResolution(outputDirectory, slideShowHeight);
        slideShowFile = checkRescaledImageExistence(slideShowFile) ? slideShowFile : null;

        //Check small thumb file.
        Integer thumbSizeSmall = (Integer) getDefault(CONFIG_PARAM_THUMB_SMALL_SIZE);
        File thumbSmallFile = imageFileData.createThumbNailFileForResolution(outputDirectory, thumbSizeSmall);
        thumbSmallFile = checkRescaledImageExistence(thumbSmallFile) ? thumbSmallFile : null;

        //Check big thumb file.
        Integer thumbSizeBig = (Integer) getDefault(CONFIG_PARAM_THUMB_BIG_SIZE);
        File thumbBigFile = imageFileData.createThumbNailFileForResolution(outputDirectory, thumbSizeBig);
        thumbBigFile = checkRescaledImageExistence(thumbBigFile) ? thumbBigFile : null;

        return new ScaledImageResources(slideShowFile, thumbBigFile, thumbSmallFile);
    }

    @Override
    public void generateRescaledImageFiles() {
        File outputDirectory = determineOutputDirectory();
        Integer slideShowHeight = determineSlideShowImageHeight();
        Integer thumbSizeBig = (Integer) getDefault(CONFIG_PARAM_THUMB_BIG_SIZE);
        Integer thumbSizeSmall = (Integer) getDefault(CONFIG_PARAM_THUMB_SMALL_SIZE);
        //Process files in batches to reduce memory footprint.
        LOG.info("Generating slide show images [Size: {}] and thumb nails [Size: {},{}] to directory [{}].", slideShowHeight, thumbSizeBig, thumbSizeSmall, outputDirectory.getAbsolutePath());
        int count = 0;
        int batchSize = 500;
        int index = 0;
        boolean finished = false;
        while (!finished)  {
            List<ImageFileData> imageDataList = imageRepositoryDAO.findImagesForRescaling(index, batchSize);
            for (ImageFileData imageData : imageDataList) {
                File imageFile = new File(imageData.getAbsolutePath(), imageData.getFilename());
                if (imageFile.exists()) {
                    count += thumbGenerator.generateScaledImages(imageData, outputDirectory, slideShowHeight, thumbSizeBig, thumbSizeSmall);
                } else {
                    LOG.debug("Removing non-existent image file [{}] in directory [{}].", imageData.getFilename(), imageData.getAbsolutePath());
                    imageRepositoryDAO.deleteImage(imageData);
                }
            }
            finished |= imageDataList.size() < batchSize;
            index += batchSize;
        }
        LOG.info("Generation of slide show images [Size: {}] and thumb nails [Size: {},{}] to directory [{}] completed. [{}] rescaled images created.", slideShowHeight, thumbSizeBig, thumbSizeSmall, outputDirectory.getAbsolutePath(), count);
    }

    /**
     * Checks whether a given image file exists in the output directory for resclaed images.
     * @param rescaledImage The {@link File} of the rescaled image.
     * @return <code>true</code> if file exists.
     */
    private boolean checkRescaledImageExistence(File rescaledImage)  {
        return rescaledImage.exists() && rescaledImage.isFile();
    }

    private void validateMediaFileDirectory(File directory) {
        Validate.notNull(directory, "Directory is null.");
        Validate.isTrue(directory.exists(), "Directory [" + directory.getAbsolutePath() + "] does not exist.");
        Validate.isTrue(directory.isDirectory(), "File [" + directory.getAbsolutePath() + "] is not a directory.");
    }

    private void validateMediaFile(File imageFile) {
        Validate.notNull(imageFile, "ImageFile is null.");
        Validate.isTrue(imageFile.exists(), "ImageFile [" + imageFile.getAbsolutePath() + "] does not exist.");
        Validate.isTrue(imageFile.isFile(), "ImageFile [" + imageFile.getAbsolutePath() + "] is not a file.");
    }

    private void scanImageFileMetaData(File imageFile, ImageFileData imageData) throws MediaFileScanException {
        if (isSupportedMediaFile(imageFile))  {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
                //Map the EXIF directory of image file
                mapEXIFIFD0Directory(imageData.getMetaData(), metadata);
                mapExifSubIFDDirectory(imageData.getMetaData(), metadata);
            } catch (ImageProcessingException ex)  {
                throw new MediaFileScanException("Extraction of metainformation for Image file [" + imageFile.getAbsolutePath() + "] failed.", ex);
            } catch (IOException ex)  {
                throw new MediaFileScanException("IO error during extraction of meta information for Image file [" + imageFile.getAbsolutePath() + "].", ex);
            }
        }
    }

    private void mapExifSubIFDDirectory(ImageMetaData metaData, Metadata exifMetadata)  {
        ExifSubIFDDirectory exifDirectory = exifMetadata.getDirectory(ExifSubIFDDirectory.class);
        if (exifDirectory != null)  {
            metaData.setHeight(exifDirectory.getInteger(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
            metaData.setWidth(exifDirectory.getInteger(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
            metaData.setCreationDate(exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
            metaData.setColorSpace(ColorSpace.byExifCode(exifDirectory.getInteger(ExifSubIFDDirectory.TAG_COLOR_SPACE)));
            ImageExposureData exposureData = metaData.getExposureData();
            exposureData.setAperture(exifDirectory.getDoubleObject(ExifSubIFDDirectory.TAG_FNUMBER));
            exposureData.setFocalLength(exifDirectory.getInteger(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
            exposureData.setIsoValue(exifDirectory.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
            exposureData.setExposureTime(exifDirectory.getDoubleObject(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
            exposureData.setWhiteBalance(WhiteBalanceMode.byExifCode(exifDirectory.getInteger(ExifSubIFDDirectory.TAG_WHITE_BALANCE_MODE)));
            exposureData.setFlashMode(FlashMode.byExifCode(exifDirectory.getInteger(ExifSubIFDDirectory.TAG_FLASH)));
        }
    }

    private void mapEXIFIFD0Directory(ImageMetaData metaData, Metadata exifMetadata) {
        ExifIFD0Directory exifD0Directory = exifMetadata.getDirectory(ExifIFD0Directory.class);
        if (exifD0Directory != null)  {
            ImageCameraData cameraData = metaData.getCameraData();
            cameraData.setManufacturer(exifD0Directory.getString(ExifIFD0Directory.TAG_MAKE));
            cameraData.setModel(exifD0Directory.getString(ExifIFD0Directory.TAG_MODEL));

            ImageSourceData sourceData = metaData.getSourceData();
            sourceData.setAuthor(exifD0Directory.getString(ExifIFD0Directory.TAG_ARTIST));
            sourceData.setCopyright(exifD0Directory.getString(ExifIFD0Directory.TAG_COPYRIGHT));
        }
    }

    private File determineOutputDirectory() {
        //Load configuration for output directory of rescaled images.
        String rescaleDirectory = configService.getConfigurationParameter(MediaFileType.IMAGE, CONFIG_PARAM_RESCALED_IMAGE_DIRECTORY);
        if (rescaleDirectory == null)  {
            rescaleDirectory = (String) getDefault(CONFIG_PARAM_RESCALED_IMAGE_DIRECTORY);
            configService.setConfigurationParameter(MediaFileType.IMAGE, CONFIG_PARAM_RESCALED_IMAGE_DIRECTORY, rescaleDirectory);
        }
        File outputDirectory = new File(rescaleDirectory);
        if (!outputDirectory.exists() || !outputDirectory.isDirectory() || !outputDirectory.canWrite())  {
            LOG.error("Invalid output directory [{}] for rescaling iamges.", rescaleDirectory);
            rescaleDirectory = (String) getDefault(CONFIG_PARAM_RESCALED_IMAGE_DIRECTORY);
            configService.setConfigurationParameter(MediaFileType.IMAGE, CONFIG_PARAM_RESCALED_IMAGE_DIRECTORY, rescaleDirectory);
        }
        return outputDirectory;
    }

    private Integer determineSlideShowImageHeight() {
        //Load configuration for slide show image size.
        Integer slideShowHeight = null;
        String slideShowHeightValue = configService.getConfigurationParameter(MediaFileType.IMAGE, CONFIG_PARAM_SLIDE_SHOW_SIZE);
        try {
            slideShowHeight = (slideShowHeightValue != null) ? Integer.valueOf(slideShowHeightValue) : null;
        } catch (NumberFormatException ex) {
            LOG.error("Invalid value [{}] for slide show image height configured.", slideShowHeightValue);
        }
        if (slideShowHeight == null)  {
            slideShowHeight = (Integer) getDefault(CONFIG_PARAM_SLIDE_SHOW_SIZE);
            configService.setConfigurationParameter(MediaFileType.IMAGE, CONFIG_PARAM_SLIDE_SHOW_SIZE, String.valueOf(slideShowHeight));
        }
        return slideShowHeight;
    }
}
