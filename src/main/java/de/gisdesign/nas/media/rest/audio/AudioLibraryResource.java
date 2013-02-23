package de.gisdesign.nas.media.rest.audio;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.repo.MediaRepository;
import de.gisdesign.nas.media.repo.audio.AudioMediaRepository;
import de.gisdesign.nas.media.rest.AbstractLibraryResource;
import de.gisdesign.nas.media.rest.CatalogEntryResourceBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * An {@link AudioLibraryResource} allows access to multiple root folders for audio files
 * in the file system.
 * @author Denis Pasek
 */
public class AudioLibraryResource extends AbstractLibraryResource<AudioFileData> {

    /**
     * The {@link AudioMediaRepository}.
     */
    private AudioMediaRepository audioRepository;

    /**
     * Constructor.
     * @param audioRepository The {@link MediaRepository}.
     * @param audioLibrary The {@link MediaFileLibrary}.
     * @param uriInfo The {@link UriInfo} of this resource.
     */
    public AudioLibraryResource(AudioMediaRepository audioRepository, MediaFileLibrary audioLibrary, UriInfo uriInfo) {
        super(audioRepository, audioLibrary, uriInfo);
        this.audioRepository = audioRepository;
    }

    @Override
    protected CatalogEntryResourceBuilder<AudioFileData> createCatalogEntryResourceBuilder() {
        return new AudioResourceBuilder(audioRepository);
    }
}
