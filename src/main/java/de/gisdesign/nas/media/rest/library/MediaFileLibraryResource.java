package de.gisdesign.nas.media.rest.library;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MediaRootDirectory;
import de.gisdesign.nas.media.repo.InvalidRootDirectoryException;
import de.gisdesign.nas.media.repo.MediaFileLibraryManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST resource representing a single {@link MediaFileLibrary} to be managed.
 * @author Denis Pasek
 */
public class MediaFileLibraryResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MediaFileLibraryResource.class);

    /**
     * The {@link MediaFileLibraryManager}.
     */
    private MediaFileLibraryManager mediaFileLibraryManager;

    /**
     * The {@link MediaFileLibrary}.
     */
    private MediaFileLibrary mediaFileLibrary;

    /**
     * The {@link MediaFileType}.
     */
    private MediaFileType mediaFileType;

    /**
     * The REST {@link UriInfo} of this resource.
     */
    private UriInfo uriInfo;

    public MediaFileLibraryResource(MediaFileLibraryManager mediaFileLibraryManager, MediaFileType mediaFileType, UriInfo uriInfo) {
        Validate.notNull(mediaFileLibraryManager, "MediaFileLibraryManager is null.");
        Validate.notNull(mediaFileType, "MediaFileType is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        this.mediaFileLibraryManager = mediaFileLibraryManager;
        this.mediaFileType = mediaFileType;
        this.uriInfo = uriInfo;
    }

    public MediaFileLibraryResource(MediaFileLibraryManager mediaFileLibraryManager, MediaFileLibrary mediaFileLibrary, UriInfo uriInfo) {
        Validate.notNull(mediaFileLibraryManager, "MediaFileLibraryManager is null.");
        Validate.notNull(mediaFileLibrary, "MediaFileLibrary is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        this.mediaFileLibraryManager = mediaFileLibraryManager;
        this.mediaFileLibrary = mediaFileLibrary;
        this.mediaFileType = mediaFileLibrary.getMediaFileType();
        this.uriInfo = uriInfo;
    }

    @Path("/{directoryName}")
    public MediaRootDirectoryResource getMediaRootDirectory(@PathParam("directoryName") String directoryName)  {
        MediaRootDirectoryResource mediaRootDirectoryResource;
        MediaRootDirectory mediaRootDirectory = mediaFileLibrary.getRootDirectory(directoryName);
        if (mediaRootDirectory != null)  {
            mediaRootDirectoryResource = new MediaRootDirectoryResource(mediaFileLibraryManager, mediaFileLibrary, mediaRootDirectory, uriInfo);
        } else {
            mediaRootDirectoryResource = new MediaRootDirectoryResource(mediaFileLibraryManager, mediaFileLibrary, directoryName, uriInfo);
        }
        LOG.debug("Created MediaRootDirectoryResource [{}] for MediaFileLibrary [{}] for MediaFileType [{}]", directoryName, mediaFileLibrary.getName(), mediaFileLibrary.getMediaFileType());
        return mediaRootDirectoryResource;
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public Response get()  {
        Response response;
        if (mediaFileLibrary != null) {
            MediaLibraryDTO mediaLibraryDTO = new MediaLibraryDTO();
            mediaLibraryDTO.setName(mediaFileLibrary.getName());
            List<MediaRootDirectoryDTO> rootDirectoryDTOs = new ArrayList<MediaRootDirectoryDTO>(mediaFileLibrary.getRootDirectories().size());
            for (MediaRootDirectory mediaRootDirectory : mediaFileLibrary.getRootDirectories()) {
                String uri = uriInfo.getAbsolutePathBuilder().path(mediaRootDirectory.getName()).build().toString();
                MediaRootDirectoryDTO rootDirectoryDTO = new MediaRootDirectoryDTO(mediaRootDirectory.getName(), mediaRootDirectory.getDirectory().getAbsolutePath(), uri);
                rootDirectoryDTOs.add(rootDirectoryDTO);
            }
            mediaLibraryDTO.setRootDirectories(rootDirectoryDTOs);
            LOG.debug("Created MediaLibrary DTO for MediaFileLibrary [{}] for MediaFileType [{}]", mediaFileLibrary.getName(), mediaFileLibrary.getMediaFileType());
            response = Response.ok(mediaLibraryDTO).build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    @DELETE
    public Response delete() {
        Response response;
        if (mediaFileLibrary != null)  {
            mediaFileLibraryManager.deleteMediaFileLibrary(mediaFileLibrary.getMediaFileType(), mediaFileLibrary.getName());
            LOG.debug("Deleted MediaFileLibrary [{}] of MediaFileType [{}].", mediaFileLibrary.getName(), mediaFileLibrary.getMediaFileType());
            response = Response.ok().build();
        } else {
            LOG.warn("Cannot delete unknown MediaFileLibrary of MediaFileType [{}].", mediaFileLibrary.getMediaFileType());
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    @PUT
    @Consumes("application/json; charset=UTF-8")
    public Response createOrUpdate(MediaLibraryDTO mediaLibrary) {
        //Validate if update request is valid
        if (mediaFileLibrary != null && !mediaFileLibrary.getName().equals(mediaLibrary.getName())) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        mediaFileLibraryManager.createMediaFileLibrary(mediaFileType, mediaLibrary.getName());
        if (mediaLibrary.getRootDirectories() != null) {
            for (MediaRootDirectoryDTO mediaRootDirectoryDTO : mediaLibrary.getRootDirectories()) {
                try {
                    mediaFileLibraryManager.addMediaFileLibraryDirectory(mediaFileType, mediaLibrary.getName(), mediaRootDirectoryDTO.getName(), mediaRootDirectoryDTO.getPath());
                } catch (InvalidRootDirectoryException ex)  {
                    throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
                }
            }
        }
        LOG.debug("Created/Updated MediaFileLibrary [{}] of MediaFileType [{}].", mediaLibrary.getName(), mediaFileType);
        return Response.created(uriInfo.getAbsolutePath()).build();
    }
}
