package de.gisdesign.nas.media.domain.audio;

import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.domain.catalog.DefaultMediaFileCatalogEntry;
import java.io.File;

/**
 * A {@link CatalogEntry} representing an audio file.
 * @author Denis Pasek
 */
public class AudioCatalogEntry extends DefaultMediaFileCatalogEntry {

    /**
     * The basic audio file meta data.
     */
    private AudioFileData audioFileData;

    /**
     * Constructor.
     * @param parent
     * @param audioFileData
     */
    public AudioCatalogEntry(CatalogEntry parent, AudioFileData audioFileData) {
        super(audioFileData.getId(), new File(audioFileData.getAbsolutePath(), audioFileData.getFilename()), parent);
        this.audioFileData = audioFileData;
    }

    public AudioFileData getAudioFileData() {
        return audioFileData;
    }
}
