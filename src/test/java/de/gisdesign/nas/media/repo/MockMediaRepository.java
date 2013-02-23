package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.MetaDataCriteriaFactory;
import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Denis Pasek
 */
public class MockMediaRepository implements MediaRepository<ImageFileData> {

    private Map<String, Map<String,ImageFileData>> imageFileDataMap = new HashMap<String, Map<String,ImageFileData>>();

    @Override
    public MediaFileType getSupportedMediaFileType() {
        return MediaFileType.IMAGE;
    }

    @Override
    public CatalogEntry createMediaFileCatalogEntry(CatalogEntry parent, ImageFileData mediaFileData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSupportedMediaFile(File mediaFile) {
        return mediaFile.getName().toLowerCase().endsWith("jpg");
    }

    @Override
    public ImageFileData loadMediaFileData(File mediaFile) {
        ImageFileData imageFileData = null;
        Map<String,ImageFileData> directory = this.imageFileDataMap.get(mediaFile.getParent());
        if (directory != null)  {
            imageFileData = directory.get(mediaFile.getName());
        }
        return imageFileData;
    }

    @Override
    public ImageFileData createMediaFileData(File mediaFile) throws MediaFileScanException {
        ImageFileData imageFileData = new ImageFileData();
        imageFileData.setAbsolutePath(mediaFile.getParentFile().getAbsolutePath());
        imageFileData.setFilename(mediaFile.getName());
        storeImageFileData(mediaFile, imageFileData);
        return imageFileData;
    }

    @Override
    public ImageFileData updateMediaFileData(ImageFileData mediaFileData) throws MediaFileScanException {
        File mediaFile = new File(mediaFileData.getAbsolutePath(), mediaFileData.getFilename());
        storeImageFileData(mediaFile, mediaFileData);
        return mediaFileData;
    }

    @Override
    public Map<String, ImageFileData> loadMediaFilesFromDirectory(File mediaDirectory) {
        Map<String, ImageFileData> directory = loadMediaFilesFromDirectoryInternal(mediaDirectory);
        return new HashMap<String, ImageFileData>(directory);
    }

    @Override
    public List<String> getMediaFileLibraryNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MediaFileLibrary getMediaFileLibrary(String libraryName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteMediaFileData(ImageFileData mediaFileData) {
        Map<String, ImageFileData> imageFileDataMap = loadMediaFilesFromDirectoryInternal(new File(mediaFileData.getAbsolutePath()));
        imageFileDataMap.remove(mediaFileData.getFilename());
    }

    @Override
    public List<ImageFileData> findMediaFilesByCriteria(MetaDataCriteria mediaMetaDataCriteria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MetaDataCriteria> loadMetaDataCriteriaOptions(MetaDataCriteria mediaMetaDataCriteria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void storeImageFileData(File mediaFile, ImageFileData imageFileData) {
        Map<String, ImageFileData> directory = loadMediaFilesFromDirectoryInternal(mediaFile.getParentFile());
        directory.put(mediaFile.getName(), imageFileData);
    }

    private Map<String, ImageFileData> loadMediaFilesFromDirectoryInternal(File mediaDirectory) {
        Map<String,ImageFileData> directory = this.imageFileDataMap.get(mediaDirectory.getAbsolutePath());
        if (directory == null)  {
            directory = new HashMap<String, ImageFileData>();
            this.imageFileDataMap.put(mediaDirectory.getAbsolutePath(), directory);
        }
        return directory;
    }

    @Override
    public ImageFileData loadMediaFileData(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long countMediaFilesMatchingCriteria(MetaDataCriteria<?> metaDataCriteria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MetaDataCriteriaFactory getMetaDataCriteriaFactory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
