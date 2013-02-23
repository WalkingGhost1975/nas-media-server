package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaRootDirectory;
import de.gisdesign.nas.media.repo.catalog.MediaDirectoryCatalogEntry;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.image.ImageMediaRepository;
import de.gisdesign.nas.media.rest.CatalogEntryFolderResource;
import de.gisdesign.nas.media.rest.CatalogEntryResourceBuilder;
import de.gisdesign.nas.media.rest.FolderDTO;
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
     * The {@link MediaFileLibrary} represent by the resource instance.
     */
    private MediaFileLibrary imageLibrary;

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
     * @param imageLibrary The {@link MediaFileLibrary}.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public ImageLibraryResource(ImageMediaRepository imageRepository, MediaFileLibrary imageLibrary, UriInfo uriInfo) {
        Validate.notNull(imageLibrary, "MediaFileLibrary for ImageLibraryResource is null.");
        Validate.notNull(imageRepository, "ImageMediaRepository is null.");
        LOG.debug("Assembling CatalogEntries of MediaFileLibrary [{}] for root directories {}.", imageLibrary.getName(), imageLibrary.getRootDirectories());
        this.imageLibrary = imageLibrary;
        this.imageRepository = imageRepository;
        this.resourceBuilder = new ImageResourceBuilder(imageRepository);
        this.uriInfo = uriInfo;
        for (MediaRootDirectory imageRootDirectory : imageLibrary.getRootDirectories()) {
            File rootDirectory = imageRootDirectory.getDirectory();
            if (rootDirectory.exists() && rootDirectory.isDirectory())  {
                LOG.debug("Creating ImageDirectoryCatalogEntry for root directory [{}] of MediaFileLibrary [{}].", rootDirectory.getAbsoluteFile(), imageLibrary.getName());
                imageFolders.put(imageRootDirectory.getName(), new MediaDirectoryCatalogEntry<ImageFileData>(this.imageRepository, null, rootDirectory));
                LOG.debug("Created ImageDirectoryCatalogEntry for root directory [{}] of MediaFileLibrary [{}] successfully.", rootDirectory.getAbsoluteFile(), imageLibrary.getName());
            } else {
                LOG.error("Skipping invalid root directory [{}] of MediaFileLibrary [{}].", rootDirectory.getAbsolutePath(), imageLibrary.getName());
            }
        }
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<FolderDTO> getFolders()  {
        LOG.debug("Creating Folders for MediaFileLibrary [{}].", this.imageLibrary.getName());
        List<FolderDTO> folders = new ArrayList<FolderDTO>(imageFolders.size());
        for (Map.Entry<String,MediaDirectoryCatalogEntry<ImageFileData>> folderEntry : imageFolders.entrySet()) {
            String uri = uriInfo.getAbsolutePathBuilder().path(folderEntry.getKey()).build().toString();
            MediaDirectoryCatalogEntry<ImageFileData> folder = folderEntry.getValue();
            folders.add(new FolderDTO(folder.getCategory(), folder.getName(), uri, folder.size()));
            LOG.debug("Created Folder for root folder [{}].", folder.getName());
        }
        LOG.debug("Created [{}] Folders for MediaFileLibrary [{}] successfully.", folders.size(), this.imageLibrary.getName());
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
}
