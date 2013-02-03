package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import java.io.File;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Denis Pasek
 */
@Service
public class MediaFileLibraryManagerImpl implements MediaFileLibraryManager {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MediaFileLibraryManagerImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public MediaFileLibrary createMediaFileLibrary(MediaFileType mediaType, String libraryName) {
        Validate.notNull(mediaType, "MediaFileType is null.");
        Validate.notNull(libraryName, "Library name is null.");
        MediaFileLibraryEntity mediaFileLibrary = loadMediaFileLibrary(mediaType, libraryName);
        if (mediaFileLibrary != null)  {
            LOG.warn("MediaFileLibrary [{}] for MediaFileType [{}] already exists", libraryName, mediaType);
        } else {
            mediaFileLibrary = new MediaFileLibraryEntity(mediaType, libraryName);
            em.persist(mediaFileLibrary);
            LOG.info("Created MediaFileLibrary [{}] for MediaFileType [{}]", libraryName, mediaType);
        }
        return mediaFileLibrary;
    }

    @Transactional
    @Override
    public void deleteMediaFileLibrary(MediaFileType mediaType, String libraryName) {
        MediaFileLibraryEntity mediaFileLibrary = loadMediaFileLibrary(mediaType, libraryName);
        if (mediaFileLibrary != null)  {
            em.remove(mediaFileLibrary);
            LOG.info("Deleted MediaFileLibrary [{}] for MediaFileType [{}]", libraryName, mediaType);
        }
    }

    @Override
    public List<String> getMediaFileLibraryNames(MediaFileType mediaType) {
        Validate.notNull(mediaType, "MediaFileType is null.");
        TypedQuery<String> query = em.createQuery("SELECT mfl.name FROM MediaFileLibraryEntity mfl WHERE mfl.mediaFileType=?1", String.class);
        query.setParameter(1, mediaType);
        List<String> libraryNames = query.getResultList();
        LOG.debug("Loaded MediaFileLibrary names {} for MediaFileType [{}]", libraryNames, mediaType);
        return libraryNames;
    }

    @Override
    public MediaFileLibrary getMediaFileLibrary(MediaFileType mediaType, String libraryName) {
        Validate.notNull(mediaType, "MediaFileType is null.");
        Validate.notNull(libraryName, "Library name is null.");
        MediaFileLibraryEntity mediaFileLibrary = loadMediaFileLibrary(mediaType, libraryName);
        return mediaFileLibrary;
    }

    @Transactional
    @Override
    public void addMediaFileLibraryDirectory(MediaFileType mediaType, String libraryName, String directoryName, String path) throws InvalidRootDirectoryException {
        Validate.notNull(mediaType, "MediaFileType is null.");
        //Validates the specified root directory.
        File rootDirectory = validateNewRootDirectory(path);
        //Load or create MediaFileLibrary for adding the new directory.
        MediaFileLibraryEntity mediaFileLibrary = loadMediaFileLibrary(mediaType, libraryName);
        if (mediaFileLibrary == null)  {
            mediaFileLibrary = new MediaFileLibraryEntity(mediaType, libraryName);
            em.persist(mediaFileLibrary);
        }
        //Add new root directory.
        mediaFileLibrary.addRootDirectory(path, rootDirectory);
        LOG.debug("Added root directory [{}] under name [{}] to MediaFileLibrary [{}] for MediaFileType [{}]", path, directoryName, libraryName, mediaType);
    }

    @Transactional
    @Override
    public boolean removeMediaFileLibraryDirectory(MediaFileType mediaType, String libraryName, String directoryName) {
        Validate.notNull(mediaType, "MediaFileType is null.");
        Validate.notEmpty(libraryName, "Library name is null.");
        Validate.notEmpty(directoryName, "Directory name is null.");
        boolean removed = false;
        MediaFileLibraryEntity mediaFileLibrary = loadMediaFileLibrary(mediaType, libraryName);
        if (mediaFileLibrary != null) {
            mediaFileLibrary.removeRootDirectory(directoryName);
            LOG.debug("Removed root directory with name [{}] to MediaFileLibrary [{}] for MediaFileType [{}]", directoryName, libraryName, mediaType);
        }
        return removed;
    }

    /**
     * Loads the root directories configured for the given {@link MediaFileType}.
     * @param mediaType The {@link MediaFileType}.
     * @return The set of root directories.
     */
    private MediaFileLibraryEntity loadMediaFileLibrary(MediaFileType mediaType, String libraryName)  {
        TypedQuery<MediaFileLibraryEntity> query = em.createQuery("SELECT mfl FROM MediaFileLibraryEntity mfl WHERE mfl.mediaFileType=?1 AND mfl.name=?2", MediaFileLibraryEntity.class);
        query.setParameter(1, mediaType);
        query.setParameter(2, libraryName);
        MediaFileLibraryEntity mediaFileLibrary = null;
        try {
            mediaFileLibrary = query.getSingleResult();
            LOG.debug("Loaded MediaFileLibrary [{}] for MediaFileType [{}]", libraryName, mediaType);
        } catch (NoResultException ex) {
            LOG.warn("No MediaFileLibrary for MediaFileType [{}] with name [{}] found.", mediaType, libraryName);
        }
        return mediaFileLibrary;
    }


    /**
     * Validates the specifed root directory if it is usable as root directory.
     * It must be a directory in the filesystem where the server has read access to.
     * @param path The root directory path as String.
     * @return The root directory as {@link File}.
     * @throws InvalidRootDirectoryException If the specified path is not a valid root directory.
     */
    private File validateNewRootDirectory(String path) throws InvalidRootDirectoryException {
        Validate.notEmpty(path, "Path is null.");
        File rootDirectory = new File(path);
        //Check if path exists.
        if (!rootDirectory.exists())  {
            throw new InvalidRootDirectoryException("Directory [" + path + "] does not exist");
        }
        //Check if path is directory.
        if (!rootDirectory.isDirectory())  {
            throw new InvalidRootDirectoryException("Path [" + path + "] does not point to directory but to a file.");
        }
        //Check if we are allowed to read this directory.
        if (!rootDirectory.canRead())  {
            throw new InvalidRootDirectoryException("Root directory [" + path + "] cannot be accessed.");
        }
        return rootDirectory;
    }

}
