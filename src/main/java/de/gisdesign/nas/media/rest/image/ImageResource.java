package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.domain.image.ImageCatalogEntry;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.domain.image.ImageMetaData;
import de.gisdesign.nas.media.repo.MediaFileScanException;
import de.gisdesign.nas.media.repo.image.ImageMediaRepository;
import de.gisdesign.nas.media.repo.image.ScaledImageResources;
import de.gisdesign.nas.media.rest.MediaFileDTO;
import java.io.File;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAX-RS REST resource representing an image file managed my the {@link ImageMediaRepository}.
 * @author Denis Pasek
 */
public class ImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImageResource.class);

    private UriInfo uriInfo;
    private ImageCatalogEntry imageEntry;
    private ImageFileData imageFileData;
    private ImageResourceBuilder resourceBuilder;
    private ImageMediaRepository imageRepository;

    public ImageResource(ImageResourceBuilder resourceBuilder, CatalogEntry catalogEntry, UriInfo uriInfo) {
        Validate.notNull(resourceBuilder, "ImageResourceBuilder is null.");
        Validate.notNull(catalogEntry, "CatalogEntry is null.");
        Validate.isTrue(ImageCatalogEntry.class.isAssignableFrom(catalogEntry.getClass()), "Expected ImageCatalogEntry type.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        this.resourceBuilder = resourceBuilder;
        this.imageRepository = resourceBuilder.getImageRepository();
        this.imageEntry = (ImageCatalogEntry) catalogEntry;
        this.imageFileData = imageEntry.getImageData();
        this.uriInfo = uriInfo;
    }

    @POST
    @Path("/tags")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveTags(List<String> tags)  {
        if (tags != null)  {
            //Copy tags into image file data.
            for (String tag : tags) {
                imageFileData.addTag(tag);
            }
            //Save updated image data.
            try {
                imageRepository.updateMediaFileData(imageFileData);
            } catch (MediaFileScanException ex)  {
                LOG.error("Exception while saving tags of image [" + imageFileData.getAbsolutePath() + "/" + imageFileData.getFilename() + "].", ex);
            }
        }
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public MediaFileDTO getImageNode() {
        String uri = uriInfo.getAbsolutePathBuilder().build().toString();
        return resourceBuilder.buildMediaFile(imageEntry, uriInfo, uri);
    }

    @GET
    @Path("/metadata")
    @Produces("application/json; charset=UTF-8")
    public ImageMetaData getImageDetails() {
        return imageEntry.getImageData().getMetaData();
    }

    @GET
    @Path("/download")
    @Produces("image/*")
    public Response getDownload() {
        File downloadFile = new File(imageFileData.getAbsolutePath(), imageFileData.getFilename());
        return createImageResponse(downloadFile);
    }

    @GET
    @Path("/slide")
    @Produces("image/*")
    public Response getSlideShowFile() {
        ScaledImageResources imageResources = resourceBuilder.getImageRepository().getScaledImageResources(imageFileData);
        File slideShowFile = imageResources.getSlideShowImage();
        return createImageResponse(slideShowFile);
    }

    @GET
    @Path("/thumbSmall")
    @Produces("image/*")
    public Response getThumbSmallFile() {
        ScaledImageResources imageResources = resourceBuilder.getImageRepository().getScaledImageResources(imageFileData);
        File thumbSmallFile = imageResources.getThumbnailSmall();
        return createImageResponse(thumbSmallFile);
    }

    @GET
    @Path("/thumbBig")
    @Produces("image/*")
    public Response getThumbBigFile() {
        ScaledImageResources imageResources = resourceBuilder.getImageRepository().getScaledImageResources(imageFileData);
        File thumbBigFile = imageResources.getThumbnailBig();
        return createImageResponse(thumbBigFile);
    }

    /**
     * Creates the REST response for an image file.
     * @param imageFile The image file.
     * @return The REST {@link Response}.
     * @throws WebApplicationException If the fiel does not exist.
     */
    private Response createImageResponse(File imageFile) throws WebApplicationException {
        if (imageFile == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        String mt = new MimetypesFileTypeMap().getContentType(imageFile);
        return Response.ok(imageFile, mt).header("Content-Disposition", "inline; filename=" + imageFile.getName()).build();
    }
}
