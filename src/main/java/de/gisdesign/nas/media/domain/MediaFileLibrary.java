package de.gisdesign.nas.media.domain;

import java.io.File;
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
     * Returns the {@link Set} of root directories.
     * @return The root directories.
     */
    public List<File> getRootDirectories();

}
