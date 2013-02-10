package de.gisdesign.nas.media.rest.library;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaRootDirectory;
import de.gisdesign.nas.media.repo.InvalidRootDirectoryException;
import de.gisdesign.nas.media.repo.MediaFileLibraryManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Denis Pasek
 */
public class MediaRootDirectoryResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MediaRootDirectoryResource.class);

    /**
     * The {@link MediaFileLibraryManager}.
     */
    private MediaFileLibraryManager mediaFileLibraryManager;

    /**
     * The {@link MediaFileLibrary}.
     */
    private MediaFileLibrary mediaFileLibrary;

    /**
     * The directory name.
     */
    private String directoryName;

    /**
     * The {@link MediaFileLibrary}.
     */
    private MediaRootDirectory mediaRootDirectory;

    /**
     * The REST {@link UriInfo} of this resource.
     */
    private UriInfo uriInfo;

    public MediaRootDirectoryResource(MediaFileLibraryManager mediaFileLibraryManager, MediaFileLibrary mediaFileLibrary, String directoryName, UriInfo uriInfo) {
        this(mediaFileLibraryManager, mediaFileLibrary, uriInfo);
        Validate.notNull(directoryName, "Directory name is null.");
        this.directoryName = directoryName;
    }

    public MediaRootDirectoryResource(MediaFileLibraryManager mediaFileLibraryManager, MediaFileLibrary mediaFileLibrary, MediaRootDirectory mediaRootDirectory, UriInfo uriInfo) {
        this(mediaFileLibraryManager, mediaFileLibrary, uriInfo);
        Validate.notNull(mediaRootDirectory, "MediaRootDirectory is null.");
        this.mediaRootDirectory = mediaRootDirectory;
        this.directoryName = mediaRootDirectory.getName();
    }

    private MediaRootDirectoryResource(MediaFileLibraryManager mediaFileLibraryManager, MediaFileLibrary mediaFileLibrary, UriInfo uriInfo) {
        Validate.notNull(mediaFileLibraryManager, "MediaFileLibraryManager is null.");
        Validate.notNull(mediaFileLibrary, "MediaFileLibrary is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        this.mediaFileLibraryManager = mediaFileLibraryManager;
        this.mediaFileLibrary = mediaFileLibrary;
        this.uriInfo = uriInfo;
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public Response get()  {
        Response response;
        if (mediaRootDirectory != null) {
            MediaRootDirectoryDTO mediaRootDirectoryDTO = new MediaRootDirectoryDTO(mediaRootDirectory.getName(), mediaRootDirectory.getDirectory().getAbsolutePath(), uriInfo.getAbsolutePath().toString());
            LOG.debug("Created MediaRootDirectory DTO for MediaFileLibrary [{}] for MediaFileType [{}]", mediaFileLibrary.getName(), mediaFileLibrary.getMediaFileType());
            response = Response.ok(mediaRootDirectoryDTO).build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    @DELETE
    public void delete() {
        mediaFileLibraryManager.removeMediaFileLibraryDirectory(mediaFileLibrary.getMediaFileType(), mediaFileLibrary.getName(), mediaRootDirectory.getName());
        LOG.debug("Deleted MediaRootDirectory [{}] in MediaFileLibrary [{}] of MediaFileType [{}].", mediaRootDirectory.getName(), mediaFileLibrary.getName(), mediaFileLibrary.getMediaFileType());
    }

    @PUT
    @Consumes("application/json; charset=UTF-8")
    public Response createOrUpdate(MediaRootDirectoryDTO rootDirectoryDTO) {
        //Validate if update request is valid
        if (!directoryName.equals(rootDirectoryDTO.getName())) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        try {
            mediaFileLibraryManager.addMediaFileLibraryDirectory(mediaFileLibrary.getMediaFileType(), mediaFileLibrary.getName(), rootDirectoryDTO.getName(), rootDirectoryDTO.getPath());
            LOG.debug("Created MediaRootDirectory [{}] in MediaFileLibrary [{}] of MediaFileType [{}].", rootDirectoryDTO.getName(), mediaFileLibrary.getName(), mediaFileLibrary.getMediaFileType());
            return Response.created(uriInfo.getAbsolutePath()).build();
        } catch (InvalidRootDirectoryException ex)  {
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        }
    }
}
