package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import java.util.List;

/**
 * Interface describing the default DAO operations for a {@link MediaRepository}.
 * @param <M> The supported type of {@link MediaFileData}.
 * @author Denis Pasek
 */
public interface MediaFileDataDAO<M extends MediaFileData> {

    /**
     * Finds the {@link MediaFileData} by ID.
     * @param id The ID of the {@link MediaFileData}.
     * @return The {@link MediaFileData} or <code>null</code> if not found.
     */
    public M findMediaFileById(Long id);

    /**
     * Finds the {@link MediaFileData} by the absolute filename of the media file.
     * @param absoluteFileName The absolute file name.
     * @return The {@link MediaFileData} or <code>null</code> if not found.
     */
    public M findMediaFileByAbsoluteFileName(String absoluteFileName);

    /**
     * Finds the {@link MediaFileData} of all media files located in the given directory.
     * @param directoryName The absolute path of the directory.
     * @return The list of {@link MediaFileData}.
     */
    public List<M> findMediaFilesByDirectory(String directoryName);

    /**
     * Finds the {@link MediaFileData} of all media files matching the given hierarchy
     * of {@link MetaDataCriteria}.
     * @param criteria The {@link MetaDataCriteria} hierarchy.
     * @return The list of {@link MediaFileData}.
     */
    public List<M> findMediaFilesByCriteria(MetaDataCriteria<?> criteria);

    /**
     * Counts the number of {@link MediaFileData} of all media files matching the given hierarchy
     * of {@link MetaDataCriteria}. Is used as shortcut instead of loading all {@link MediaFileData}
     * using {@link #findMediaFilesByCriteria(de.gisdesign.nas.media.domain.MetaDataCriteria)}.
     * @param criteria The {@link MetaDataCriteria} hierarchy.
     * @return The number of matching {@link MediaFileData}.
     */
    public long countMediaFilesMatchingCriteria(MetaDataCriteria<?> criteria);

    /**
     * Loads the available option values of the leaf {@link MetaDataCriteria} defined in the
     * {@link MetaDataCriteria} hierarchy.
     * @param <T> The type of the option values.
     * @param criteria The {@link MetaDataCriteria} hierarchy.
     * @return The list of available option values.
     */
    public <T> List<T> loadMediaFileCriteriaValues(MetaDataCriteria<T> criteria);

    /**
     * Saves or updates the given {@link MediaFileData} to the persistent store.
     * @param mediaFile The {@link MediaFileData} to save.
     * @return The saved {@link MediaFileData}.
     */
    public M saveMediaFile(M mediaFile);

    /**
     * Deletes the given {@link MediaFileData} from the persistent store.
     * @param mediaFileData The {@link MediaFileData} to delete.
     */
    public void deleteMediaFile(M mediaFileData);

}
