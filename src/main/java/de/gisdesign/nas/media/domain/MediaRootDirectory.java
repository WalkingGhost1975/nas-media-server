package de.gisdesign.nas.media.domain;

import java.io.File;

/**
 * Interface describing a root directory of a {@link MediaFileLibrary}.
 * @author Denis Pasek
 */
public interface MediaRootDirectory {

    /**
     * Returns the unique name of the root directory in the scope of the {@link MediaFileLibrary}
     * that contains this directory.
     * @return The name of the directory.
     */
    public String getName();

    /**
     * Returns the {@link File} representing the root directory.
     * @return The root directory as {@link File}.
     */
    public File getDirectory();

}
