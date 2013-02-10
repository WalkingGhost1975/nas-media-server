package de.gisdesign.nas.media.rest.audio;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.repo.audio.AudioMediaRepository;
import de.gisdesign.nas.media.rest.FolderDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link AudioLibrariesResource} allows access to multiple root folders for audio files
 * in the file system.
 * @author Denis Pasek
 */
public class AudioLibrariesResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AudioLibrariesResource.class);

    /**
     * The {@link AudioMediaRepository}.
     */
    private AudioMediaRepository audioRepository;

    /**
     * The REST {@link UriInfo} of this resource.
     */
    private UriInfo uriInfo;

    /**
     * Constructor.
     * @param audioRepository The {@link AudioMediaRepository}.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public AudioLibrariesResource(AudioMediaRepository audioRepository, UriInfo uriInfo) {
        Validate.notNull(audioRepository, "AudioMediaRepository is null.");
        this.audioRepository = audioRepository;
        this.uriInfo = uriInfo;
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<FolderDTO> getLibraries()  {
        LOG.debug("Creating Folders for MediaFileLibraries.");
        List<String> audioLibraryNames = audioRepository.getMediaFileLibraryNames();
        List<FolderDTO> folders = new ArrayList<FolderDTO>(audioLibraryNames.size());
        for (String libraryName : audioLibraryNames) {
            String uri = uriInfo.getAbsolutePathBuilder().path(libraryName).build().toString();
            MediaFileLibrary audioLibrary = audioRepository.getMediaFileLibrary(libraryName);
            folders.add(new FolderDTO("directory", libraryName, uri, audioLibrary.getRootDirectories().size()));
            LOG.debug("Created Folder for MediaFileLibrary [{}].", audioLibrary.getName());
        }
        LOG.debug("Created [{}] Folders for MediaFileLibraries successfully.", folders.size());
        return folders;
    }

    @Path("/{libraryName}")
    public AudioLibraryResource getLibrary(@PathParam("libraryName") String libraryName)  {
        MediaFileLibrary audioLibrary = audioRepository.getMediaFileLibrary(libraryName);
        AudioLibraryResource audioLibraryResource = null;
        if (audioLibrary != null)  {
            audioLibraryResource = new AudioLibraryResource(audioRepository, audioLibrary, uriInfo);
            LOG.debug("Created AudioLibraryResource for MediaFileLibrary [{}].", libraryName);
        } else {
            LOG.warn("MediaFileLibrary [{}] unknown.", libraryName);
        }
        return audioLibraryResource;
    }
}
