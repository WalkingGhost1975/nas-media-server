package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MediaRootDirectory;
import java.io.File;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Entity for storing a root directory of a {@link MediaFileLibraryEntity}.
 * @author Denis Pasek
 */
@Entity
@Table(name="MEDIA_DIRECTORY", uniqueConstraints={
    @UniqueConstraint(columnNames={"FILE_TYPE","NAME"})
})
public class MediaRootDirectoryEntity implements MediaRootDirectory, Serializable {
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

    @Column(name="NAME", length=16)
    private String name;

    @Column(name="DIRECTORY", length=1024)
    private String directory;

    public MediaRootDirectoryEntity() {
    }

    public MediaRootDirectoryEntity(String name, MediaFileType mediaFileType, String path) {
        this.name = name;
        this.mediaFileType = mediaFileType;
        this.directory = path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public File getDirectory() {
        return new File(directory);
    }

    public MediaFileType getMediaFileType() {
        return mediaFileType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
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
        final MediaRootDirectoryEntity other = (MediaRootDirectoryEntity) obj;
        if (this.mediaFileType != other.mediaFileType) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.directory == null) ? (other.directory != null) : !this.directory.equals(other.directory)) {
            return false;
        }
        return true;
    }


}
