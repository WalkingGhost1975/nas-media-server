package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MediaRootDirectory;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.Validate;

/**
 * JPA entity implementing the {@link MediaFileLibrary} interface.
 * @author Denis Pasek
 */
@Entity
@Table(name="MEDIA_LIBRARY", uniqueConstraints={
    @UniqueConstraint(columnNames={"MEDIA_TYPE", "NAME"})
})
public class MediaFileLibraryEntity implements MediaFileLibrary, Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="MEDIA_TYPE", nullable=false)
    private MediaFileType mediaFileType;

    @Column(name="NAME", nullable=false)
    private String name;

    /**
     * The list of root directories belonging to this {@link MediaFileLibrary}.
     */
    @OneToMany(fetch= FetchType.EAGER, cascade= CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="LIBRARY_ID", nullable=false)
    private Set<MediaRootDirectoryEntity> mediaFileRootDirectories = new HashSet<MediaRootDirectoryEntity>();

    /**
     * Constructor.
     */
    public MediaFileLibraryEntity() {
    }

    public MediaFileLibraryEntity(MediaFileType mediaFileType, String name) {
        Validate.notNull(mediaFileType, "MediaFileType is null.");
        Validate.notEmpty(name, "Name is empty.");
        this.mediaFileType = mediaFileType;
        this.name = name;
    }

    @Override
    public MediaFileType getMediaFileType() {
        return mediaFileType;
    }

    @Override
    public String getName() {
        return name;
    }

    public void addRootDirectory(String name, File directory)  {
        Validate.notNull(name, "Name of root directory is null.");
        Validate.notNull(directory, "Root directory is null.");
        Validate.isTrue(directory.isDirectory(), "Root directory [" + directory.getAbsolutePath() + "] is not a directory.");
        MediaRootDirectoryEntity rootDirectory = (MediaRootDirectoryEntity) getRootDirectory(name);
        if (rootDirectory != null)  {
            rootDirectory.updateDirectory(directory.getAbsolutePath());
        } else {
            rootDirectory = new MediaRootDirectoryEntity(name, this.mediaFileType, directory.getAbsolutePath());
        }
        this.mediaFileRootDirectories.add(rootDirectory);
    }

    public boolean removeRootDirectory(String name)  {
        Validate.notNull(name, "Name of root directory is null.");
        boolean removed = false;
        for (Iterator<MediaRootDirectoryEntity> it = this.mediaFileRootDirectories.iterator(); it.hasNext(); ) {
            MediaRootDirectoryEntity rootDirectory = it.next();
            if (rootDirectory.getName().equals(name))  {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public MediaRootDirectory getRootDirectory(String directoryName) {
        MediaRootDirectory mediaRootDirectory = null;
        List<MediaRootDirectory> rootDirectories = getRootDirectories();
        for (MediaRootDirectory rootDirectory : rootDirectories) {
            if (directoryName.equals(rootDirectory.getName()))  {
                mediaRootDirectory = rootDirectory;
                break;
            }
        }
        return mediaRootDirectory;
    }

    @Override
    public List<MediaRootDirectory> getRootDirectories() {
        return new ArrayList<MediaRootDirectory>(this.mediaFileRootDirectories);
    }
}
