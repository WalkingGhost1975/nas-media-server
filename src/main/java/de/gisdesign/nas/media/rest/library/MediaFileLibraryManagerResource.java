package de.gisdesign.nas.media.rest.library;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.MediaFileLibraryManager;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Root REST resource wrapping the {@link MediaFileLibraryManager}.
 * @author Denis Pasek
 */
@Component
@Path("/libraries")
public class MediaFileLibraryManagerResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MediaFileLibraryManagerResource.class);
    /**
     * The {@link MediaFileLibraryManager}.
     */
    @Autowired
    private MediaFileLibraryManager mediaFileLibraryManager;
    /**
     * The REST {@link UriInfo} of this resource.
     */
    @Context
    private UriInfo uriInfo;

    public MediaFileLibraryManagerResource() {
    }

    @PostConstruct
    public void init() {
    }

    @GET
    @Path("/{mediaFileType}")
    @Produces("application/json; charset=UTF-8")
    public List<MediaLibraryLocationDTO> getMediaFileLibraries(@PathParam("mediaFileType") String mediaFileTypeString)  {
        List<MediaLibraryLocationDTO> mediaLibrariesDTOs;
        try {
            MediaFileType mediaFileType = MediaFileType.valueOf(mediaFileTypeString.toUpperCase());
            List<String> libraryNames = mediaFileLibraryManager.getMediaFileLibraryNames(mediaFileType);
            mediaLibrariesDTOs = new ArrayList<MediaLibraryLocationDTO>(libraryNames.size());
            for (String libraryName : libraryNames) {
                String uri = uriInfo.getAbsolutePathBuilder().path(libraryName).build().toString();
                mediaLibrariesDTOs.add(new MediaLibraryLocationDTO(libraryName, uri));
            }
        } catch (IllegalArgumentException ex) {
            LOG.warn("Illegal MediaFileType [" + mediaFileTypeString + "].");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return mediaLibrariesDTOs;
    }

    @Path("/{mediaFileType}/{libraryName}")
    public MediaFileLibraryResource getMediaFileLibrary(@PathParam("mediaFileType") String mediaFileTypeString, @PathParam("libraryName") String libraryName)  {
        MediaFileLibraryResource mediaFileLibraryResource = null;
        try {
            MediaFileType mediaFileType = MediaFileType.valueOf(mediaFileTypeString.toUpperCase());
            MediaFileLibrary mediaFileLibrary = mediaFileLibraryManager.getMediaFileLibrary(mediaFileType, libraryName);
            if (mediaFileLibrary != null) {
                mediaFileLibraryResource = new MediaFileLibraryResource(mediaFileLibraryManager, mediaFileLibrary, uriInfo);
            } else {
                mediaFileLibraryResource = new MediaFileLibraryResource(mediaFileLibraryManager, mediaFileType, uriInfo);
            }
            LOG.debug("Created MediaFileLibraryResource for MediaFileLibrary [{}] for MediaFileType [{}]", libraryName, mediaFileType);
        } catch (IllegalArgumentException ex) {
            LOG.warn("Illegal MediaFileType [" + mediaFileTypeString + "].");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return mediaFileLibraryResource;
    }
}
