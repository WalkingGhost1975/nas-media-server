package de.gisdesign.nas.media.domain.audio;

import de.gisdesign.nas.media.domain.MediaFileData;
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
public class AudioFileData implements MediaFileData, Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Column(name="LOCATION", length=1024)
    private String absolutePath;

    @Column(name="FILENAME")
    private String filename;

    @Column(name="RATING")
    private Integer rating;

    @Column(name="LAST_MODIFIED")
    private Long lastModified;

    @Embedded
    private AudioMetaData metaData = new AudioMetaData();

    public Long getId() {
        return id;
    }

    @Override
    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public boolean hasChanged(long modificationTimestamp)  {
        return (this.lastModified != null) ? (modificationTimestamp != this.lastModified) : true;
    }

    public AudioMetaData getMetaData() {
        if (metaData == null)  {
            this.metaData = new AudioMetaData();
        }
        return metaData;
    }

    @Override
    public Integer getRating() {
        return rating;
    }

    @Override
    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
