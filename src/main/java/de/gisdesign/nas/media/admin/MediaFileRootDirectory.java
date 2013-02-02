package de.gisdesign.nas.media.admin;

import de.gisdesign.nas.media.domain.MediaFileType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Denis Pasek
 */
@Entity
@Table(name="MEDIA_ROOT_FOLDER")
public class MediaFileRootDirectory implements Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="FILE_TYPE", length=16)
    private MediaFileType mediaFileType;

    @Column(name="DIRECTORY", length=1024)
    private String directory;

    public MediaFileRootDirectory() {
    }

    public MediaFileRootDirectory(MediaFileType mediaFileType, String path) {
        this.mediaFileType = mediaFileType;
        this.directory = path;
    }

    public Long getId() {
        return id;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public MediaFileType getMediaFileType() {
        return mediaFileType;
    }

    public void setMediaFileType(MediaFileType mediaFileType) {
        this.mediaFileType = mediaFileType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.mediaFileType != null ? this.mediaFileType.hashCode() : 0);
        hash = 67 * hash + (this.directory != null ? this.directory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MediaFileRootDirectory other = (MediaFileRootDirectory) obj;
        if (this.mediaFileType != other.mediaFileType) {
            return false;
        }
        if ((this.directory == null) ? (other.directory != null) : !this.directory.equals(other.directory)) {
            return false;
        }
        return true;
    }


}
