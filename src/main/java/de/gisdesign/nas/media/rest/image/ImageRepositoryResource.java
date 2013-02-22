package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.domain.MetaDataCriteriaFactory;
import de.gisdesign.nas.media.domain.catalog.CriteriaFolderCatalogEntry;
import de.gisdesign.nas.media.domain.catalog.CriteriaFolderHierachy;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.image.ImageFileScanner;
import de.gisdesign.nas.media.repo.image.ImageMediaRepository;
import de.gisdesign.nas.media.rest.CatalogDTO;
import de.gisdesign.nas.media.rest.CatalogEntryFolderResource;
import de.gisdesign.nas.media.rest.CatalogEntryResourceBuilder;
import de.gisdesign.nas.media.rest.audio.AudioFileResource;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
 * Root REST resource for managing image files. Supports a standard image
 * catalog model based on the file system structure and configurable catalogs
 * which use image meta data managed in a image meta data repository, e.g. a
 * database.
 *
 * @author pasekdbh
 */
@Component
@Path("/images")
public class ImageRepositoryResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImageRepositoryResource.class);
    /**
     * The {@link ImageMediaRepository}.
     */
    @Autowired
    private ImageMediaRepository imageRepository;
    /**
     * The {@link ImageFileScanner}.
     */
    @Autowired
    private ImageFileScanner imageFileScanner;

    /**
     * The {@link MetaDataCriteriaFactory}.
     */
    @Autowired
    private MetaDataCriteriaFactory criteriaFactory;

    /**
     * The {@link CatalogEntryResourceBuilder}.
     */
    private ImageResourceBuilder resourceBuilder;
    /**
     * The REST {@link UriInfo} of this resource.
     */
    @Context
    private UriInfo uriInfo;

    public ImageRepositoryResource() {
    }

    @PostConstruct
    public void init() {
        LOG.info("Initialized ImageRepository REST service!");
        resourceBuilder = new ImageResourceBuilder(imageRepository);
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<CatalogDTO> getCatalogs() {
        List<CatalogDTO> catalogs = new ArrayList<CatalogDTO>(2);
        String byFoldersUri = uriInfo.getAbsolutePathBuilder().path("byLibrary").build().toString();
        catalogs.add(new CatalogDTO("byLibrary", byFoldersUri));
        String byYearsAndMonthUri = uriInfo.getAbsolutePathBuilder().path("byYearAndMonth").build().toString();
        catalogs.add(new CatalogDTO("byYearAndMonth", byYearsAndMonthUri));
        String byTagUri = uriInfo.getAbsolutePathBuilder().path("byTag").build().toString();
        catalogs.add(new CatalogDTO("byTag", byTagUri));
        return catalogs;
    }

    /**
     * Returns the subresource for the configured image library consisting of
     * multiple root directories. This resource will be navigable simply by the
     * filesystem directories.
     *
     * @return The REST resource representing the image library organized by
     * filesystem directories.
     */
    @Path("/byLibrary")
    public ImageLibrariesResource getLibrary() {
        LOG.debug("Creating ImageLibrariesResource.");
        return new ImageLibrariesResource(imageRepository, uriInfo);
    }

    @Path("/byYearAndMonth")
    public CatalogEntryFolderResource getByYearAndMonth() {
        CriteriaFolderHierachy hierarchy = new CriteriaFolderHierachy(criteriaFactory);
        hierarchy.addCriteria("image:creationDateYear");
        hierarchy.addCriteria("image:creationDateMonth");
        CriteriaFolderCatalogEntry<ImageFileData,Integer> catalogEntry = new CriteriaFolderCatalogEntry<ImageFileData,Integer>(imageRepository, hierarchy);
        return new CatalogEntryFolderResource(resourceBuilder, catalogEntry, uriInfo);
    }

    @Path("/byTag")
    public CatalogEntryFolderResource getByTag() {
        CriteriaFolderHierachy hierarchy = new CriteriaFolderHierachy(criteriaFactory);
        hierarchy.addCriteria("image:tags");
        CriteriaFolderCatalogEntry<ImageFileData,Integer> catalogEntry = new CriteriaFolderCatalogEntry<ImageFileData,Integer>(imageRepository, hierarchy);
        return new CatalogEntryFolderResource(resourceBuilder, catalogEntry, uriInfo);
    }

    /**
     * Retrievs the REST resource for an audio file identified by the unique ID of the file.
     * @param id The ID of the media file.
     * @return The {@link AudioFileResource}.
     */
    @Path("/file/{id}")
    public ImageResource getById(@PathParam("id") Long id) {
        LOG.debug("Creating AudioFileResource.");
        ImageFileData imageFileData = imageRepository.loadMediaFileData(id);
        if (imageFileData == null)  {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new ImageResource(resourceBuilder, imageFileData, uriInfo);
    }

    @POST
    @Path("/scan")
    public void getScanLibrary() {
        //Scan all libraries
        LOG.info("Image file scan manually triggered on ImageRepositoryResource.");
        this.imageFileScanner.scanMediaFileLibrary();
    }
}
