package de.gisdesign.nas.media.domain.audio;

import de.gisdesign.nas.media.domain.MediaFileEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Denis Pasek
 */
@Entity
@Table(name="AUDIO_FILE")
public class AudioFileData extends MediaFileEntity implements Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    @Embedded
    private AudioMetaData metaData = new AudioMetaData();

    public AudioMetaData getMetaData() {
        if (metaData == null)  {
            this.metaData = new AudioMetaData();
        }
        return metaData;
    }
}
