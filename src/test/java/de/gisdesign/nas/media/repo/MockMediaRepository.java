package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
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

    private boolean deletedOrphans = false;

    private Map<String, Map<String,ImageFileData>> imageFileDataMap = new HashMap<String, Map<String,ImageFileData>>();

    public boolean isDeletedOrphans() {
        return deletedOrphans;
    }

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
        return createMediaFileData(mediaFile, null);
    }

    @Override
    public ImageFileData createMediaFileData(File mediaFile, Long syncId) throws MediaFileScanException {
        ImageFileData imageFileData = new ImageFileData();
        imageFileData.setAbsolutePath(mediaFile.getParentFile().getAbsolutePath());
        imageFileData.setFilename(mediaFile.getName());
        imageFileData.setSyncId(syncId);
        storeImageFileData(mediaFile, imageFileData);
        return imageFileData;
    }

    @Override
    public ImageFileData updateMediaFileData(ImageFileData mediaFileData, Long syncId) throws MediaFileScanException {
        File mediaFile = new File(mediaFileData.getAbsolutePath(), mediaFileData.getFilename());
        mediaFileData.setSyncId(syncId);
        storeImageFileData(mediaFile, mediaFileData);
        return mediaFileData;
    }

    @Override
    public Map<String, ImageFileData> loadMediaFilesFromDirectory(File mediaDirectory) {
        Map<String,ImageFileData> directory = this.imageFileDataMap.get(mediaDirectory.getAbsolutePath());
        if (directory == null)  {
            directory = new HashMap<String, ImageFileData>();
            this.imageFileDataMap.put(mediaDirectory.getAbsolutePath(), directory);
        }
        return directory;
    }

    @Override
    public List<ImageFileData> findMediaFilesByCriteria(MetaDataCriteria mediaMetaDataCriteria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MetaDataCriteria> loadMetaDataCriteriaOptions(MetaDataCriteria mediaMetaDataCriteria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteOrphanedMediaFiles(Long syncId) {
        this.deletedOrphans = true;
    }

    private void storeImageFileData(File mediaFile, ImageFileData imageFileData) {
        Map<String,ImageFileData> directory = this.imageFileDataMap.get(mediaFile.getParentFile().getAbsolutePath());
        if (directory == null)  {
            directory = new HashMap<String, ImageFileData>();
            this.imageFileDataMap.put(mediaFile.getParentFile().getAbsolutePath(), directory);
        }
        directory.put(mediaFile.getName(), imageFileData);
    }

    @Override
    public List<String> getMediaFileLibraryNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MediaFileLibrary getMediaFileLibrary(String libraryName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
