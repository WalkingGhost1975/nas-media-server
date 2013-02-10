package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.domain.image.ImageCatalogEntry;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.domain.image.ImageTag;
import de.gisdesign.nas.media.repo.image.ImageMediaRepository;
import de.gisdesign.nas.media.repo.image.ScaledImageResources;
import de.gisdesign.nas.media.rest.CatalogEntryResourceBuilder;
import de.gisdesign.nas.media.rest.MediaFileDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Denis Pasek
 */
public class ImageResourceBuilder implements CatalogEntryResourceBuilder<ImageFileData> {

    /**
     * The underlying {@link ImageMediaRepository}.
     */
    private ImageMediaRepository imageRepository;

    /**
     * Constructor.
     * @param imageRepository The udnerlying {@link ImageMediaRepository} to use.
     */
    public ImageResourceBuilder(ImageMediaRepository imageRepository) {
        Validate.notNull(imageRepository, "ImageMediaRepository is null.");
        this.imageRepository = imageRepository;
    }

    public ImageMediaRepository getImageRepository() {
        return imageRepository;
    }

    @Override
    public MediaFileDTO buildMediaFile(CatalogEntry catalogEntry, UriInfo uriInfo) {
        Validate.notNull(catalogEntry, "CatalogEntry is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        String uri = uriInfo.getAbsolutePathBuilder().path(catalogEntry.getName()).build().toString();
        return buildMediaFile(catalogEntry, uriInfo, uri);
    }

    public MediaFileDTO buildMediaFile(CatalogEntry catalogEntry, UriInfo uriInfo, String uri) {
        Validate.notNull(catalogEntry, "CatalogEntry is null.");
        Validate.notNull(uri, "Uri is null.");
        ImageCatalogEntry imageEntry = (ImageCatalogEntry) catalogEntry;

        ImageFileData imageFileData = imageEntry.getImageData();
        ScaledImageResources imageResources = imageRepository.getScaledImageResources(imageFileData);

        ImageDTO image = new ImageDTO(catalogEntry.getName(), uri, imageEntry.getLastModified(), imageEntry.getSize());
        //Set download link
        image.setDownloadUri(uriInfo.getAbsolutePathBuilder().path("/download").build().toString());
        //Set metadata link
        image.setMetadataUri(uriInfo.getAbsolutePathBuilder().path("/metadata").build().toString());

        //Set slide show image resource URI
        if (imageResources.getSlideShowImage() != null)  {
            image.setSlideShowUri(uriInfo.getAbsolutePathBuilder().path("/slide").build().toString());
        }

        //Set big thumb image resource URI
        if (imageResources.getThumbnailSmall() != null)  {
            image.setThumbSmallUri(uriInfo.getAbsolutePathBuilder().path("/thumbSmall").build().toString());
        }

        //Set big thumb image resource URI
        if (imageResources.getThumbnailBig() != null)  {
            image.setThumbBigUri(uriInfo.getAbsolutePathBuilder().path("/thumbBig").build().toString());
        }
        //Add image tags
        List<String> tags = new ArrayList<String>(imageFileData.getTags().size());
        for (ImageTag imageTag : imageFileData.getTags()) {
            tags.add(imageTag.getText());
        }
        image.setTags(tags);

        return image;
    }

    @Override
    public Object buildMediaFileResource(CatalogEntry catalogEntry, UriInfo uriInfo) {
        Validate.notNull(catalogEntry, "CatalogEntry is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        return new ImageResource(this, catalogEntry, uriInfo);
    }

}
