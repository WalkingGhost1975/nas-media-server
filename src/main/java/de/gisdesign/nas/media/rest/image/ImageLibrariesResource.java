package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.image.ImageMediaRepository;
import de.gisdesign.nas.media.rest.AbstractLibrariesResource;
import de.gisdesign.nas.media.rest.AbstractLibraryResource;
import javax.ws.rs.core.UriInfo;

/**
 * An {@link ImageLibraryResource} allows access to multiple root folders for images
 * in the file system.
 * @author Denis Pasek
 */
public class ImageLibrariesResource extends AbstractLibrariesResource<ImageFileData> {

    private ImageMediaRepository imageRepository;

    /**
     * Constructor.
     * @param imageRepository The {@link ImageMediaRepository}.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public ImageLibrariesResource(ImageMediaRepository imageRepository, UriInfo uriInfo) {
        super(imageRepository, uriInfo);
        this.imageRepository = imageRepository;
    }

    @Override
    protected AbstractLibraryResource<ImageFileData> createLibraryResource(MediaFileLibrary library, UriInfo uriInfo) {
        return new ImageLibraryResource(imageRepository, library, uriInfo);
    }
}
