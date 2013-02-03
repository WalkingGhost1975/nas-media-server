package de.gisdesign.nas.media.domain;

import java.util.List;
import java.util.Set;

/**
 * Interface for a {@link MediaFileLibrary}. Such a library is a set of multiple
 * root directories of the filesystem. Only media files being present in these
 * libraries can be accessed.
 * @author Denis Pasek
 */
public interface MediaFileLibrary {

    /**
     * Returns the {@link MediaFileType} of this {@link MediaFileLibrary}.
     * @return The {@link MediaFileType}.
     */
    public MediaFileType getMediaFileType();

    /**
     * Returns the name of this {@link MediaFileLibrary}.
     * @return The name of the library.
     */
    public String getName();

    /**
     * Returns the {@link MediaRootDirectory} with the specified unique directory name.
     * @param directoryName The configured unique name of the directory in this library.
     * @return The {@link MediaRootDirectory} or <code>null</code> if not present..
     */
    public MediaRootDirectory getRootDirectory(String directoryName);

    /**
     * Returns the {@link Set} of {@link MediaRootDirectory}s.
     * @return The root directories.
     */
    public List<MediaRootDirectory> getRootDirectories();

}
