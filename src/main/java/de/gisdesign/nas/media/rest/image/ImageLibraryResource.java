package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.image.ImageMediaRepository;
import de.gisdesign.nas.media.rest.AbstractLibraryResource;
import de.gisdesign.nas.media.rest.CatalogEntryResourceBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * An {@link ImageLibraryResource} allows access to multiple root folders for images
 * in the file system.
 * @author Denis Pasek
 */
public class ImageLibraryResource extends AbstractLibraryResource<ImageFileData> {

    /**
     * The {@link ImageMediaRepository}.
     */
    private ImageMediaRepository imageRepository;

    /**
     * Constructor.
     * @param imageRepository The {@link ImageMediaRepository}.
     * @param imageLibrary The {@link MediaFileLibrary}.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public ImageLibraryResource(ImageMediaRepository imageRepository, MediaFileLibrary imageLibrary, UriInfo uriInfo) {
        super(imageRepository, imageLibrary, uriInfo);
        this.imageRepository = imageRepository;
    }

    @Override
    protected CatalogEntryResourceBuilder<ImageFileData> createCatalogEntryResourceBuilder() {
        return new ImageResourceBuilder(imageRepository);
    }
}
