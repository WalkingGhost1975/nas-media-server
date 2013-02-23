package de.gisdesign.nas.media.rest.audio;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.repo.audio.AudioMediaRepository;
import de.gisdesign.nas.media.rest.AbstractLibrariesResource;
import de.gisdesign.nas.media.rest.AbstractLibraryResource;
import javax.ws.rs.core.UriInfo;

/**
 * An {@link AudioLibrariesResource} allows access to multiple root folders for audio files
 * in the file system.
 * @author Denis Pasek
 */
public class AudioLibrariesResource extends AbstractLibrariesResource<AudioFileData> {

    /**
     * The {@link AudioMediaRepository}.
     */
    private AudioMediaRepository audioRepository;

    /**
     * Constructor.
     * @param audioRepository The {@link AudioMediaRepository}.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public AudioLibrariesResource(AudioMediaRepository audioRepository, UriInfo uriInfo) {
        super(audioRepository, uriInfo);
        this.audioRepository = audioRepository;
    }

    @Override
    protected AbstractLibraryResource<AudioFileData> createLibraryResource(MediaFileLibrary library, UriInfo uriInfo) {
        return new AudioLibraryResource(audioRepository, library, uriInfo);
    }
}
