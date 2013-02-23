package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.MetaDataCriteriaFactory;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract base class for {@link MediaRepository} implementations.
 * @param <M> The supported type of {@link MediaFileData}.
 * @author Denis Pasek
 */
public abstract class AbstractMediaRepository<M extends MediaFileData> implements MediaRepository<M> {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMediaRepository.class);

    @Autowired
    private MediaFileLibraryManager mediaFileLibraryManager;

    @Autowired
    private MetaDataCriteriaFactory metaDataCriteriaFactory;

    /**
     * The supported {@link MediaFileType}.
     */
    private MediaFileType mediaFileType;

    public AbstractMediaRepository(MediaFileType mediaFileType) {
        Validate.notNull(mediaFileType, "MediaFileType is null.");
        this.mediaFileType = mediaFileType;
    }

    @Override
    public MediaFileType getSupportedMediaFileType() {
        return this.mediaFileType;
    }

    @Override
    public MetaDataCriteriaFactory getMetaDataCriteriaFactory() {
        return metaDataCriteriaFactory;
    }

    @Override
    public List<String> getMediaFileLibraryNames() {
        return this.mediaFileLibraryManager.getMediaFileLibraryNames(getSupportedMediaFileType());
    }

    @Override
    public MediaFileLibrary getMediaFileLibrary(String libraryName) {
        return this.mediaFileLibraryManager.getMediaFileLibrary(getSupportedMediaFileType(), libraryName);
    }

    @Override
    public M loadMediaFileData(Long id) {
        Validate.notNull(id, "ID is null.");
        return getMediaFileDataDAO().findMediaFileById(id);
    }

    @Override
    public M loadMediaFileData(File mediaFile) {
        validateMediaFile(mediaFile);
        return getMediaFileDataDAO().findMediaFileByAbsoluteFileName(mediaFile.getAbsolutePath());
    }

    @Transactional
    @Override
    public M updateMediaFileData(M mediaFileData) throws MediaFileScanException {
        File mediaFile = new File(mediaFileData.getAbsolutePath(), mediaFileData.getFilename());
        //Only rescan meta data if timestamp has changed.
        if (mediaFileData.hasChanged(mediaFile.lastModified())) {
            scanMediaFileMetaData(mediaFile, mediaFileData);
        }
        return getMediaFileDataDAO().saveMediaFile(mediaFileData);
    }

    @Transactional
    @Override
    public void deleteMediaFileData(M mediaFileData) {
        getMediaFileDataDAO().deleteMediaFile(mediaFileData);
    }

    @Override
    public Map<String,M> loadMediaFilesFromDirectory(File directory) {
        validateMediaFileDirectory(directory);
        List<M> mediaFileDataList = getMediaFileDataDAO().findMediaFilesByDirectory(directory.getAbsolutePath());
        Map<String,M> mediaFileDataMap = new HashMap<String,M>(mediaFileDataList.size()*2);
        LOG.debug("Loaded {} MediaFileData for media files in directory [{}].", mediaFileDataList.size(), directory.getAbsolutePath());
        for (M mediaFileData : mediaFileDataList) {
            mediaFileDataMap.put(mediaFileData.getFilename(), mediaFileData);
        }
        return mediaFileDataMap;
    }

    @Override
    public List<M> findMediaFilesByCriteria(MetaDataCriteria<?> criteria) {
        List<M> audioFiles = getMediaFileDataDAO().findMediaFilesByCriteria(criteria);
        LOG.debug("Loaded [{}] Media files for MetaDataCriteria [{}]", audioFiles.size(), criteria.dumpHierarchy());
        return audioFiles;
    }

    @Override
    public <T> List<T> loadMetaDataCriteriaOptions(MetaDataCriteria<T> metaDataCriteria) {
        List<T> criteriaValues = getMediaFileDataDAO().loadMediaFileCriteriaValues(metaDataCriteria);
        LOG.debug("Loaded MetaDataCriteriaValues {} for MetaDataCriteria [{}]", criteriaValues, metaDataCriteria.dumpHierarchy());
        return criteriaValues;
    }

    @Override
    public long countMediaFilesMatchingCriteria(MetaDataCriteria<?> metaDataCriteria) {
        long count = getMediaFileDataDAO().countMediaFilesMatchingCriteria(metaDataCriteria);
        LOG.debug("MetaDataCriteria [{}] has [{}] matching Audio files.", metaDataCriteria.dumpHierarchy(), count);
        return count;
    }

    /**
     * Template method. Should return a suitable {@link MediaFileDataDAO} implementation
     * for the {@link MediaRepository} implementation.
     * @return The {@link MediaFileDataDAO} implementation.
     */
    protected abstract MediaFileDataDAO<M> getMediaFileDataDAO();

    /**
     * Template method. Should scan the gievn media file and update the {@link MediaFileData} accordingly.
     * It is important to update the last modification timestamp and size of the file as well.
     * @param mediaFile The media file to scan.
     * @param mediaFileData The {@link MediaFileData} to update.
     * @throws MediaFileScanException If the file cannot be scanned.
     */
    protected abstract void scanMediaFileMetaData(File mediaFile, M mediaFileData) throws MediaFileScanException;

    protected final void validateMediaFile(File audioFile) {
        Validate.notNull(audioFile, "MediaFile is null.");
        Validate.isTrue(audioFile.exists(), "MediaFile [" + audioFile.getAbsolutePath() + "] does not exist.");
        Validate.isTrue(audioFile.isFile(), "MediaFile [" + audioFile.getAbsolutePath() + "] is not a file.");
    }

    protected final void validateMediaFileDirectory(File directory) {
        Validate.notNull(directory, "Directory is null.");
        Validate.isTrue(directory.exists(), "Directory [" + directory.getAbsolutePath() + "] does not exist.");
        Validate.isTrue(directory.isDirectory(), "File [" + directory.getAbsolutePath() + "] is not a directory.");
    }

}
