package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.admin.AdministrationService;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.catalog.CriteriaFolderCatalogEntry;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.image.ImageFileScanner;
import de.gisdesign.nas.media.repo.image.ImageMediaRepository;
import de.gisdesign.nas.media.rest.Catalog;
import de.gisdesign.nas.media.rest.CatalogEntryFolderResource;
import de.gisdesign.nas.media.rest.CatalogEntryResourceBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Root REST resource for managing image media. Supports a standard image
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
     * The {@link MediaRepositoryService}.
     */
    @Autowired
    private ImageMediaRepository mediaRepository;
    /**
     * The {@link ImageFileScanner}.
     */
    @Autowired
    private ImageFileScanner imageFileScanner;

    @Autowired
    private AdministrationService adminService;
    /**
     * The {@link CatalogEntryResourceBuilder}.
     */
    private CatalogEntryResourceBuilder<ImageFileData> resourceBuilder;
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
        resourceBuilder = new ImageResourceBuilder(mediaRepository);
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<Catalog> getCatalogs() {
        List<Catalog> catalogs = new ArrayList<Catalog>(2);
        String byFoldersUri = uriInfo.getAbsolutePathBuilder().path("byFolder").build().toString();
        catalogs.add(new Catalog("byFolder", byFoldersUri));
        String byYearsAndMonthUri = uriInfo.getAbsolutePathBuilder().path("byYearAndMonth").build().toString();
        catalogs.add(new Catalog("byYearAndMonth", byYearsAndMonthUri));
        String byTagUri = uriInfo.getAbsolutePathBuilder().path("byTag").build().toString();
        catalogs.add(new Catalog("byTag", byTagUri));
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
    @Path("/byFolder")
    public ImageLibraryResource getLibrary() {
        MediaFileLibrary imageFileLibrary = adminService.getMediaFileLibrary(MediaFileType.IMAGE);
        LOG.debug("Returning ImageLibraryResource for root folders {}", imageFileLibrary.getRootDirectories());
        return new ImageLibraryResource(mediaRepository, imageFileLibrary.getRootDirectories(), uriInfo);
    }

    @Path("/byYearAndMonth")
    public CatalogEntryFolderResource getByYearAndMonth() {
        MetaDataCriteria yearCriteria = new MetaDataCriteria(MediaFileType.IMAGE, "creationDateYear");
        MetaDataCriteria monthCriteria = new MetaDataCriteria(MediaFileType.IMAGE, "creationDateMonth");
        yearCriteria.setChildCriteria(monthCriteria);
        CriteriaFolderCatalogEntry<ImageFileData> catalogEntry = new CriteriaFolderCatalogEntry<ImageFileData>(mediaRepository, null, yearCriteria);
        return new CatalogEntryFolderResource(resourceBuilder, catalogEntry, uriInfo);
    }

    @Path("/byTag")
    public CatalogEntryFolderResource getByTag() {
        MetaDataCriteria tagLevel1Criteria = new MetaDataCriteria(MediaFileType.IMAGE, "tags");
        CriteriaFolderCatalogEntry<ImageFileData> catalogEntry = new CriteriaFolderCatalogEntry<ImageFileData>(mediaRepository, null, tagLevel1Criteria);
        return new CatalogEntryFolderResource(resourceBuilder, catalogEntry, uriInfo);
    }

    @POST
    @Path("/scan")
    public void getScanLibrary() {
        MediaFileLibrary imageFileLibrary = adminService.getMediaFileLibrary(MediaFileType.IMAGE);
        this.imageFileScanner.scanMediaFileLibrary(imageFileLibrary);
    }
}
