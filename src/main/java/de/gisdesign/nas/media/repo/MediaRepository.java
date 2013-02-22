package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Interface used to encapsulated the operation on media files. Implementations
 * of these interfaces are specific to the various media types, e.g. images, music files
 * and videos.
 * @param <M> The supported type of {@link MediaFileData} for the {@link MediaRepository}.
 * @author Denis Pasek
 */
public interface MediaRepository<M extends MediaFileData> {

    /**
     * Retrieves the names of all available {@link MediaFileLibrary}s.
     * @return The name of the {@link MediaFileLibrary}s.
     */
    public List<String> getMediaFileLibraryNames();

    /**
     * Retrieves the configured {@link MediaFileLibrary}. {@link MediaFileLibrary}s are used to aggregate multiple directories
     * into a library.
     * @param libraryName The name of the {@link MediaFileLibrary}.
     * @return The {@link MediaFileLibrary}
     */
    public MediaFileLibrary getMediaFileLibrary(String libraryName);

    /**
     * Returns the supported {@link MediaFileType} of this {@link MediaRepository}.
     * @return The {@link MediaFileType}.
     */
    public MediaFileType getSupportedMediaFileType();

    /**
     * Creates a {@link CatalogEntry} representing a media file.
     * @param parent The {@link CatalogEntry} representing the folder containing the media file.
     * @param mediaFileData The {@link MediaFileData}.
     * @return The created {@link CatalogEntry} or <code>null</code> if media file no longer exists.
     */
    public CatalogEntry createMediaFileCatalogEntry(CatalogEntry parent, M mediaFileData);

    /**
     * Checks whether the specified file is a supported media file in the context
     * of the concrete implemtation supporting certain media types.
     * @param mediaFile The media {@link File} to be checked.
     * @return <code>true</code> if the media file is supported.
     */
    public boolean isSupportedMediaFile(File mediaFile);

    /**
     * Loads the basic information from the backing database for the
     * given media file by the ID.
     * @param id The id of the media file.
     * @return The basic media file information or <code>null</code> if file
     * cannot be found.
     */
    public M loadMediaFileData(Long id);

    /**
     * Loads the basic information from the backing database for the
     * given media file.
     * @param mediaFile The media file.
     * @return The basic media file information or <code>null</code> if file
     * cannot be found.
     */
    public M loadMediaFileData(File mediaFile);

    /**
     * Creates the media file meta information for the given media file and stores them
     * in the backing data base..
     * @param mediaFile The media file.
     * @return The freshly created basic media file information.
     * @throws MediaFileScanException The extraction of the meta information for the given media file failed.
     */
    public M createMediaFileData(File mediaFile) throws MediaFileScanException;

    /**
     * Updates/synchronizes the media file meta information against the underlying file
     * and stores the updated information into the backing database.
     * @param mediaFileData The media file toupdate.
     * @return The updated basic media file information.
     * @throws MediaFileScanException The extraction of the meta information for the given media file failed.
     */
    public M updateMediaFileData(M mediaFileData) throws MediaFileScanException;

    /**
     * Deletes the media file meta information.
     * @param mediaFileData The media file toupdate.
     */
    public void deleteMediaFileData(M mediaFileData);

    /**
     * Loads all the basic media file information for a media directory.
     * @param mediaDirectory The media directory.
     * @return The basic media file information for all supported media files in the given
     * directory.
     */
    public Map<String,M> loadMediaFilesFromDirectory(File mediaDirectory);

    /**
     * Finds all media files matching the given meta data criteria.
     * @param mediaMetaDataCriteria The leaf of the meta data criteria tree.
     * @return The matching media file information.
     */
    public List<M> findMediaFilesByCriteria(MetaDataCriteria<?> mediaMetaDataCriteria);

    /**
     * Loads the available options for a meta data criteria. Uses the parent criteria for
     * filtering and the leaf criteria which should not have a value set as option
     * criteria.
     * @param <T> The value type of the {@link MetaDataCriteria}.
     * @param metaDataCriteria The leaf of the meta data criteria tree.
     * @return The list of available options of the leaf criteria of the meta data criteria tree.
     */
    public <T> List<T> loadMetaDataCriteriaOptions(MetaDataCriteria<T> metaDataCriteria);

}
