package de.gisdesign.nas.media.admin;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
public class AdministrationServiceImpl implements AdministrationService {

    private static final Logger LOG = LoggerFactory.getLogger(AdministrationServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public MediaFileLibrary getMediaFileLibrary(MediaFileType mediaType) {
        Set<MediaFileRootDirectory> rootDirectories = loadRootDirectories(mediaType);
        List<File> rootDirectoryFiles = new ArrayList<File>(rootDirectories.size());
        for (MediaFileRootDirectory rootDirectory : rootDirectories) {
            rootDirectoryFiles.add(new File(rootDirectory.getDirectory()));
        }
        return new DefaultMediaFileLibrary(rootDirectoryFiles);
    }

    @Transactional
    @Override
    public void addMediaFileLibraryDirectory(MediaFileType mediaType, String path) throws InvalidRootDirectoryException {
        Validate.notNull(mediaType, "MediaType is null.");
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
        Set<MediaFileRootDirectory> rootDirectories = loadRootDirectories(mediaType);
        MediaFileRootDirectory newRootDirectory = new MediaFileRootDirectory(mediaType, rootDirectory.getAbsolutePath());
        if (!rootDirectories.contains(newRootDirectory))  {
            em.persist(newRootDirectory);
        }
    }

    @Transactional
    @Override
    public boolean removeMediaFileLibraryDirectory(MediaFileType mediaType, String path) {
        Validate.notNull(mediaType, "MediaType is null.");
        Validate.notEmpty(path, "Path is null.");
        File rootDirectory = new File(path);
        Query query = em.createQuery("DELETE FROM MediaFileRootDirectory rd WHERE rd.mediaFileType=?1 AND rd.path=?2");
        query.setParameter(1, mediaType);
        query.setParameter(2, rootDirectory.getAbsolutePath());
        int count = query.executeUpdate();
        return count > 0;
    }

    private Set<MediaFileRootDirectory> loadRootDirectories(MediaFileType mediaType)  {
        TypedQuery<MediaFileRootDirectory> query = em.createQuery("SELECT rd from MediaFileRootDirectory rd WHERE rd.mediaFileType=?1", MediaFileRootDirectory.class);
        query.setParameter(1, mediaType);
        List<MediaFileRootDirectory> folders = query.getResultList();
        return new HashSet<MediaFileRootDirectory>(folders);
    }

    @Override
    public String getConfigurationParameter(MediaFileType mediaFileType, String parameterName) {
        MediaRepositoryConfigurationParameter configParameter = loadConfigurationParameter(mediaFileType, parameterName);
        return (configParameter != null) ? configParameter.getValue() : null;
    }

    @Transactional
    @Override
    public void setConfigurationParameter(MediaFileType mediaFileType, String parameterName, String parameterValue) {
        MediaRepositoryConfigurationParameter configParameter = loadConfigurationParameter(mediaFileType, parameterName);
        if (configParameter != null)  {
            configParameter.setValue(parameterValue);
        } else {
            configParameter = new MediaRepositoryConfigurationParameter();
            configParameter.setMediaFileType(mediaFileType);
            configParameter.setName(parameterName);
            configParameter.setValue(parameterValue);
            em.persist(configParameter);
        }
    }

    private MediaRepositoryConfigurationParameter loadConfigurationParameter(MediaFileType mediaFileType, String parameterName) {
        TypedQuery<MediaRepositoryConfigurationParameter> query = em.createQuery("SELECT cp FROM MediaRepositoryConfigurationParameter cp WHERE cp.mediaFileType=?1 AND cp.name=?2", MediaRepositoryConfigurationParameter.class);
        query.setParameter(1, mediaFileType);
        query.setParameter(2, parameterName);
        MediaRepositoryConfigurationParameter configParameter = null;
        try {
            configParameter = query.getSingleResult();
        } catch (NoResultException ex)  {
            LOG.debug("Configuration parameter [{}] for media file type [{}] not found.", parameterName, mediaFileType);
        }
        return configParameter;
    }
}
