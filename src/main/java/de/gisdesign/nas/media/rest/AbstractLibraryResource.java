package de.gisdesign.nas.media.rest;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaRootDirectory;
import de.gisdesign.nas.media.repo.MediaRepository;
import de.gisdesign.nas.media.repo.catalog.MediaDirectoryCatalogEntry;
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
 * {@link AbstractLibraryResource} used as template. Allows access to multiple root folders for audio files
 * in the file system.
 * @param <M> The {@link MediaFileData} type supported.
 * @author Denis Pasek
 */
public abstract class AbstractLibraryResource<M extends MediaFileData> {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractLibraryResource.class);

    /**
     * The {@link MediaRepository}.
     */
    private MediaRepository<M> mediaRepository;

    /**
     * The {@link MediaFileLibrary} represent by the resource instance.
     */
    private MediaFileLibrary mediaLibrary;

    /**
     * The map of {@link MediaDirectoryCatalogEntry}s representing the root folders
     * of the audio library.
     */
    private Map<String, MediaDirectoryCatalogEntry<M>> libraryFolders = new HashMap<String, MediaDirectoryCatalogEntry<M>>();

    /**
     * The REST {@link UriInfo} of this resource.
     */
    private UriInfo uriInfo;

    /**
     * Constructor.
     * @param mediaRepository The {@link MediaRepository}.
     * @param mediaLibrary The {@link MediaFileLibrary}.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public AbstractLibraryResource(MediaRepository<M> mediaRepository, MediaFileLibrary mediaLibrary, UriInfo uriInfo) {
        Validate.notNull(mediaLibrary, "MediaFileLibrary for LibraryResource is null.");
        Validate.notNull(mediaRepository, "MediaRepository is null.");
        LOG.debug("Assembling CatalogEntries of MediaFileLibrary [{}] for root directories {}.", mediaLibrary.getName(), mediaLibrary.getRootDirectories());
        this.mediaLibrary = mediaLibrary;
        this.mediaRepository = mediaRepository;
        this.uriInfo = uriInfo;
        for (MediaRootDirectory mediaRootDirectory : mediaLibrary.getRootDirectories()) {
            File rootDirectory = mediaRootDirectory.getDirectory();
            if (rootDirectory.exists() && rootDirectory.isDirectory())  {
                LOG.debug("Creating MediaDirectoryCatalogEntry for root directory [{}] of MediaFileLibrary [{}].", rootDirectory.getAbsoluteFile(), mediaLibrary.getName());
                libraryFolders.put(mediaRootDirectory.getName(), new MediaDirectoryCatalogEntry<M>(this.mediaRepository, null, rootDirectory));
                LOG.debug("Created MediaDirectoryCatalogEntry for root directory [{}] of MediaFileLibrary [{}] successfully.", rootDirectory.getAbsoluteFile(), mediaLibrary.getName());
            } else {
                LOG.error("Skipping invalid root directory [{}] of MediaFileLibrary [{}].", rootDirectory.getAbsolutePath(), mediaLibrary.getName());
            }
        }
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<FolderDTO> getFolders()  {
        LOG.debug("Creating Folders for MediaFileLibrary [{}].", this.mediaLibrary.getName());
        List<FolderDTO> folders = new ArrayList<FolderDTO>(libraryFolders.size());
        for (Map.Entry<String,MediaDirectoryCatalogEntry<M>> folderEntry : libraryFolders.entrySet()) {
            String uri = uriInfo.getAbsolutePathBuilder().path(folderEntry.getKey()).build().toString();
            MediaDirectoryCatalogEntry<M> folder = folderEntry.getValue();
            folders.add(new FolderDTO(folder.getCategory(), folder.getName(), uri, folder.size()));
            LOG.debug("Created Folder for root folder [{}].", folder.getName());
        }
        LOG.debug("Created [{}] Folders for MediaFileLibrary [{}] successfully.", folders.size(), this.mediaLibrary.getName());
        return folders;
    }

    @Path("/{id}")
    public CatalogEntryFolderResource getRootFolder(@PathParam("id") String id)  {
        MediaDirectoryCatalogEntry<M> rootFolderCatalogEntry = this.libraryFolders.get(id);
        CatalogEntryFolderResource folderResource = null;
        if (rootFolderCatalogEntry != null)  {
            folderResource = new CatalogEntryFolderResource(createCatalogEntryResourceBuilder(), rootFolderCatalogEntry, uriInfo);
            LOG.debug("Created AudioFolderResource for root folder [{}] with ID [{}]", rootFolderCatalogEntry.getName(), id);
        } else {
            LOG.warn("No root folder with ID [{}] found.", id);
        }
        return folderResource;
    }

    /**
     * Template method to be implemented by subclass. Must create an appropriate {@link CatalogEntryResourceBuilder}
     * used to build the sub resources for the media files.
     * @return The {@link CatalogEntryResourceBuilder}.
     */
    protected abstract CatalogEntryResourceBuilder<M> createCatalogEntryResourceBuilder();
}
