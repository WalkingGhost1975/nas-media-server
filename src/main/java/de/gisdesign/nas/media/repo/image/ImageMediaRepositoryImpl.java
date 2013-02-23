package de.gisdesign.nas.media.repo.image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import de.gisdesign.nas.media.admin.ConfigurationService;
import de.gisdesign.nas.media.domain.MediaFileType;
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
import de.gisdesign.nas.media.repo.AbstractMediaRepository;
import de.gisdesign.nas.media.repo.MediaFileDataDAO;
import de.gisdesign.nas.media.repo.MediaFileScanException;
import de.gisdesign.nas.media.repo.MediaRepository;
import static de.gisdesign.nas.media.repo.image.Configuration.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link MediaRepository} implementation for Image media files.
 * @author Denis Pasek
 */
@Component
public class ImageMediaRepositoryImpl extends AbstractMediaRepository<ImageFileData> implements ImageMediaRepository {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImageMediaRepositoryImpl.class);

    @Autowired
    private ConfigurationService configService;

    @Autowired
    private ImageRepositoryDAO imageRepositoryDAO;

    @Autowired
    private ThumbGenerator thumbGenerator;

    public ImageMediaRepositoryImpl() {
        super(MediaFileType.IMAGE);
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

    @Transactional
    @Override
    public ImageFileData createMediaFileData(File imageFile) throws MediaFileScanException {
        validateMediaFile(imageFile);
        ImageFileData imageData;
        if (isSupportedMediaFile(imageFile)) {
            imageData = new ImageFileData();
            imageData.setAbsolutePath(imageFile.getParent());
            imageData.setFilename(imageFile.getName());
            scanMediaFileMetaData(imageFile, imageData);
            imageData = imageRepositoryDAO.saveMediaFile(imageData);
        } else {
            throw new MediaFileScanException("Unsupported Image media file [" + imageFile.getAbsolutePath() + "]");
        }
        return imageData;
    }

    @Override
    public boolean isSupportedMediaFile(File imageFile) {
        validateMediaFile(imageFile);
        //TODO: Extend check for supported image files.
        return imageFile.getName().toLowerCase().endsWith("jpg");
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
                    imageRepositoryDAO.deleteMediaFile(imageData);
                }
            }
            finished |= imageDataList.size() < batchSize;
            index += batchSize;
        }
        LOG.info("Generation of slide show images [Size: {}] and thumb nails [Size: {},{}] to directory [{}] completed. [{}] rescaled images created.", slideShowHeight, thumbSizeBig, thumbSizeSmall, outputDirectory.getAbsolutePath(), count);
    }

    @Override
    protected MediaFileDataDAO<ImageFileData> getMediaFileDataDAO() {
        return imageRepositoryDAO;
    }

    @Override
    protected void scanMediaFileMetaData(File mediaFile, ImageFileData mediaFileData) throws MediaFileScanException {
        if (isSupportedMediaFile(mediaFile))  {
            mediaFileData.setLastModified(mediaFile.lastModified());
            mediaFileData.setSize(mediaFile.length());
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(mediaFile);
                //Map the EXIF directory of image file
                mapEXIFIFD0Directory(mediaFileData.getMetaData(), metadata);
                mapExifSubIFDDirectory(mediaFileData.getMetaData(), metadata);
            } catch (ImageProcessingException ex)  {
                throw new MediaFileScanException("Extraction of metainformation for Image file [" + mediaFile.getAbsolutePath() + "] failed.", ex);
            } catch (IOException ex)  {
                throw new MediaFileScanException("IO error during extraction of meta information for Image file [" + mediaFile.getAbsolutePath() + "].", ex);
            }
        }
    }

    /**
     * Checks whether a given image file exists in the output directory for resclaed images.
     * @param rescaledImage The {@link File} of the rescaled image.
     * @return <code>true</code> if file exists.
     */
    private boolean checkRescaledImageExistence(File rescaledImage)  {
        return rescaledImage.exists() && rescaledImage.isFile();
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
