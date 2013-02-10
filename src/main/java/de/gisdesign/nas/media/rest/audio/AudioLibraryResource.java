package de.gisdesign.nas.media.rest.audio;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaRootDirectory;
import de.gisdesign.nas.media.domain.audio.AudioCatalogEntry;
import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.domain.catalog.MediaDirectoryCatalogEntry;
import de.gisdesign.nas.media.repo.audio.AudioMediaRepository;
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
 * An {@link AudioLibraryResource} allows access to multiple root folders for audio files
 * in the file system.
 * @author Denis Pasek
 */
public class AudioLibraryResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AudioLibraryResource.class);

    /**
     * The {@link AudioMediaRepository}.
     */
    private AudioMediaRepository audioRepository;

    /**
     * The {@link MediaFileLibrary} represent by the resource instance.
     */
    private MediaFileLibrary audioLibrary;

    /**
     * The {@link CatalogEntryResourceBuilder}.
     */
    private CatalogEntryResourceBuilder<AudioFileData> resourceBuilder;

    /**
     * The map of {@link AudioCatalogEntry}s representing the root folders
     * of the audio library.
     */
    private Map<String, MediaDirectoryCatalogEntry<AudioFileData>> audioFolders = new HashMap<String, MediaDirectoryCatalogEntry<AudioFileData>>();

    /**
     * The REST {@link UriInfo} of this resource.
     */
    private UriInfo uriInfo;

    /**
     * Constructor.
     * @param audioRepository The {@link AudioMediaRepository}.
     * @param audioLibrary The {@link MediaFileLibrary}.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public AudioLibraryResource(AudioMediaRepository audioRepository, MediaFileLibrary audioLibrary, UriInfo uriInfo) {
        Validate.notNull(audioLibrary, "MediaFileLibrary for AudioLibraryResource is null.");
        Validate.notNull(audioRepository, "AudioMediaRepository is null.");
        LOG.debug("Assembling CatalogEntries of MediaFileLibrary [{}] for root directories {}.", audioLibrary.getName(), audioLibrary.getRootDirectories());
        this.audioLibrary = audioLibrary;
        this.audioRepository = audioRepository;
        this.resourceBuilder = new AudioResourceBuilder(audioRepository);
        this.uriInfo = uriInfo;
        for (MediaRootDirectory audioRootDirectory : audioLibrary.getRootDirectories()) {
            File rootDirectory = audioRootDirectory.getDirectory();
            if (rootDirectory.exists() && rootDirectory.isDirectory())  {
                LOG.debug("Creating AudioDirectoryCatalogEntry for root directory [{}] of MediaFileLibrary [{}].", rootDirectory.getAbsoluteFile(), audioLibrary.getName());
                audioFolders.put(audioRootDirectory.getName(), new MediaDirectoryCatalogEntry<AudioFileData>(this.audioRepository, null, rootDirectory));
                LOG.debug("Created AudioDirectoryCatalogEntry for root directory [{}] of MediaFileLibrary [{}] successfully.", rootDirectory.getAbsoluteFile(), audioLibrary.getName());
            } else {
                LOG.error("Skipping invalid root directory [{}] of MediaFileLibrary [{}].", rootDirectory.getAbsolutePath(), audioLibrary.getName());
            }
        }
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<FolderDTO> getFolders()  {
        LOG.debug("Creating Folders for MediaFileLibrary [{}].", this.audioLibrary.getName());
        List<FolderDTO> folders = new ArrayList<FolderDTO>(audioFolders.size());
        for (Map.Entry<String,MediaDirectoryCatalogEntry<AudioFileData>> folderEntry : audioFolders.entrySet()) {
            String uri = uriInfo.getAbsolutePathBuilder().path(folderEntry.getKey()).build().toString();
            MediaDirectoryCatalogEntry<AudioFileData> folder = folderEntry.getValue();
            folders.add(new FolderDTO(folder.getCategory(), folder.getName(), uri, folder.size()));
            LOG.debug("Created Folder for root folder [{}].", folder.getName());
        }
        LOG.debug("Created [{}] Folders for MediaFileLibrary [{}] successfully.", folders.size(), this.audioLibrary.getName());
        return folders;
    }

    @Path("/{id}")
    public CatalogEntryFolderResource getRootFolder(@PathParam("id") String id)  {
        MediaDirectoryCatalogEntry<AudioFileData> rootFolderCatalogEntry = this.audioFolders.get(id);
        CatalogEntryFolderResource folderResource = null;
        if (rootFolderCatalogEntry != null)  {
            folderResource = new CatalogEntryFolderResource(resourceBuilder, rootFolderCatalogEntry, uriInfo);
            LOG.debug("Created AudioFolderResource for root folder [{}] with ID [{}]", rootFolderCatalogEntry.getName(), id);
        } else {
            LOG.warn("No root folder with ID [{}] found.", id);
        }
        return folderResource;
    }
}
