package de.gisdesign.nas.media.rest;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.repo.MediaRepository;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract super class for the REST resource used to handle the set of {@link MediaFileLibrary}s.
 * @param <M>
 * @author Denis Pasek
 */
public abstract class AbstractLibrariesResource<M extends MediaFileData> {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractLibrariesResource.class);
    /**
     * The {@link MediaRepository}.
     */
    private MediaRepository<M> mediaRepository;
    /**
     * The REST {@link UriInfo} of this resource.
     */
    private UriInfo uriInfo;

    public AbstractLibrariesResource(MediaRepository<M> mediaRepository, UriInfo uriInfo) {
        Validate.notNull(mediaRepository, "AudioMediaRepository is null.");
        this.mediaRepository = mediaRepository;
        this.uriInfo = uriInfo;
    }

    @GET
    @Produces(value = "application/json; charset=UTF-8")
    public List<FolderDTO> getLibraries() {
        LOG.debug("Creating Folders for MediaFileLibraries.");
        List<String> libraryNames = mediaRepository.getMediaFileLibraryNames();
        List<FolderDTO> folders = new ArrayList<FolderDTO>(libraryNames.size());
        for (String libraryName : libraryNames) {
            String uri = uriInfo.getAbsolutePathBuilder().path(libraryName).build().toString();
            MediaFileLibrary library = mediaRepository.getMediaFileLibrary(libraryName);
            folders.add(new FolderDTO("directory", libraryName, uri, library.getRootDirectories().size()));
            LOG.debug("Created Folder for MediaFileLibrary [{}].", library.getName());
        }
        LOG.debug("Created [{}] Folders for MediaFileLibraries successfully.", folders.size());
        return folders;
    }

    @Path(value = "/{libraryName}")
    public AbstractLibraryResource<M> getLibrary(@PathParam(value = "libraryName") String libraryName) {
        MediaFileLibrary library = mediaRepository.getMediaFileLibrary(libraryName);
        AbstractLibraryResource<M> libraryResource = null;
        if (library != null) {
            libraryResource = createLibraryResource(library, uriInfo);
            LOG.debug("Created AudioLibraryResource for MediaFileLibrary [{}].", libraryName);
        } else {
            LOG.warn("MediaFileLibrary [{}] unknown.", libraryName);
        }
        return libraryResource;
    }

    protected abstract AbstractLibraryResource<M> createLibraryResource(MediaFileLibrary library, UriInfo uriInfo);
}
