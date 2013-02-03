package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileLibrary;

/**
 * Functional excpetion if an invlaid root directory for a {@link MediaFileLibrary}
 * is configured.
 * @author Denis Pasek
 */
public class InvalidRootDirectoryException extends Exception {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance of
     * <code>InvalidRootDirectoryException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public InvalidRootDirectoryException(String msg) {
        super(msg);
    }
}
