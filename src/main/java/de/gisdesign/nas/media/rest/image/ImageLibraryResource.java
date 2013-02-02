package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.domain.catalog.MediaDirectoryCatalogEntry;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.image.ImageMediaRepository;
import de.gisdesign.nas.media.rest.CatalogEntryFolderResource;
import de.gisdesign.nas.media.rest.CatalogEntryResourceBuilder;
import de.gisdesign.nas.media.rest.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link ImageLibraryResource} allows access to multiple root folders for images
 * in the file system.
 * @author Denis Pasek
 */
public class ImageLibraryResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImageLibraryResource.class);

    /**
     * The {@link ImageMediaRepository}.
     */
    private ImageMediaRepository imageRepository;

    /**
     * The {@link CatalogEntryResourceBuilder}.
     */
    private CatalogEntryResourceBuilder<ImageFileData> resourceBuilder;

    /**
     * The map of {@link ImageDirectoryCatalogEntry}s representing the root folders
     * of the image library.
     */
    private Map<String, MediaDirectoryCatalogEntry<ImageFileData>> imageFolders = new HashMap<String, MediaDirectoryCatalogEntry<ImageFileData>>();

    /**
     * The REST {@link UriInfo} of this resource.
     */
    private UriInfo uriInfo;

    /**
     * Constructor.
     * @param imageRepository The {@link ImageMediaRepository}.
     * @param rootFolders The list of root directory.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public ImageLibraryResource(ImageMediaRepository imageRepository, List<File> rootFolders, UriInfo uriInfo) {
        Validate.notNull(rootFolders, "Root folders for ImageLibraryResource are null.");
        Validate.notNull(imageRepository, "ImageMediaRepository is null.");
        LOG.debug("Assembling CatalogEntries for root folders {}.", rootFolders);
        this.imageRepository = imageRepository;
        this.resourceBuilder = new ImageResourceBuilder(imageRepository);
        this.uriInfo = uriInfo;
        for (File folder : rootFolders) {
            if (folder.exists() && folder.isDirectory())  {
                LOG.debug("Creating ImageDirectoryCatalogEntry for root folder [{}].", folder.getAbsoluteFile());
                imageFolders.put(generateImageFolderId(folder), new MediaDirectoryCatalogEntry<ImageFileData>(this.imageRepository, null, folder));
                LOG.debug("Created ImageDirectoryCatalogEntry for root folder [{}] successfully.", folder.getAbsoluteFile());
            } else {
                LOG.error("Skipping invalid root folder [{}] for image library.", folder.getAbsolutePath());
            }
        }
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<Folder> getFolders()  {
        LOG.debug("Creating Folder DTOs for ImageLibrary.");
        List<Folder> folders = new ArrayList<Folder>(imageFolders.size());
        for (Map.Entry<String,MediaDirectoryCatalogEntry<ImageFileData>> folderEntry : imageFolders.entrySet()) {
            String uri = uriInfo.getAbsolutePathBuilder().path(folderEntry.getKey()).build().toString();
            MediaDirectoryCatalogEntry<ImageFileData> folder = folderEntry.getValue();
            folders.add(new Folder(folder.getCategory(), folder.getName(), uri, folder.size()));
            LOG.debug("Created Folder DTO for root folder [{}].", folder.getName());
        }
        LOG.debug("Created [{}] Folder DTOs for ImageLibrary successfully.", folders.size());
        return folders;
    }

    @Path("/{id}")
    public CatalogEntryFolderResource getRootFolder(@PathParam("id") String id)  {
        MediaDirectoryCatalogEntry<ImageFileData> rootFolderCatalogEntry = this.imageFolders.get(id);
        CatalogEntryFolderResource folderResource = null;
        if (rootFolderCatalogEntry != null)  {
            folderResource = new CatalogEntryFolderResource(resourceBuilder, rootFolderCatalogEntry, uriInfo);
            LOG.debug("Created ImageFolderResource for root folder [{}] with ID [{}]", rootFolderCatalogEntry.getName(), id);
        } else {
            LOG.warn("No root folder with ID [{}] found.", id);
        }
        return folderResource;
    }

    /**
     * Generates a unique reproducible folder ID.
     * @param folder The folder to genrate the ID.
     * @return The folder ID.
     */
    private String generateImageFolderId(File folder)  {
        return DigestUtils.md5Hex(folder.getAbsolutePath());
    }
}
