package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.repo.image.ImageMediaRepository;
import de.gisdesign.nas.media.rest.FolderDTO;
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
 * An {@link ImageLibraryResource} allows access to multiple root folders for images
 * in the file system.
 * @author Denis Pasek
 */
public class ImageLibrariesResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImageLibrariesResource.class);

    /**
     * The {@link ImageMediaRepository}.
     */
    private ImageMediaRepository imageRepository;

    /**
     * The REST {@link UriInfo} of this resource.
     */
    private UriInfo uriInfo;

    /**
     * Constructor.
     * @param imageRepository The {@link ImageMediaRepository}.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public ImageLibrariesResource(ImageMediaRepository imageRepository, UriInfo uriInfo) {
        Validate.notNull(imageRepository, "ImageMediaRepository is null.");
        this.imageRepository = imageRepository;
        this.uriInfo = uriInfo;
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<FolderDTO> getLibraries()  {
        LOG.debug("Creating Folders for MediaFileLibraries.");
        List<String> imageLibraryNames = imageRepository.getMediaFileLibraryNames();
        List<FolderDTO> folders = new ArrayList<FolderDTO>(imageLibraryNames.size());
        for (String libraryName : imageLibraryNames) {
            String uri = uriInfo.getAbsolutePathBuilder().path(libraryName).build().toString();
            MediaFileLibrary imageLibrary = imageRepository.getMediaFileLibrary(libraryName);
            folders.add(new FolderDTO("directory", libraryName, uri, imageLibrary.getRootDirectories().size()));
            LOG.debug("Created Folder for MediaFileLibrary [{}].", imageLibrary.getName());
        }
        LOG.debug("Created [{}] Folders for MediaFileLibraries successfully.", folders.size());
        return folders;
    }

    @Path("/{libraryName}")
    public ImageLibraryResource getLibrary(@PathParam("libraryName") String libraryName)  {
        MediaFileLibrary imageLibrary = imageRepository.getMediaFileLibrary(libraryName);
        ImageLibraryResource imageLibraryResource = null;
        if (imageLibrary != null)  {
            imageLibraryResource = new ImageLibraryResource(imageRepository, imageLibrary, uriInfo);
            LOG.debug("Created ImageLibraryResource for MediaFileLibrary [{}].", libraryName);
        } else {
            LOG.warn("MediaFileLibrary [{}] unknown.", libraryName);
        }
        return imageLibraryResource;
    }
}
