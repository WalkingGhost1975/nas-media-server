package de.gisdesign.nas.media.repo;

/**
 * Exception to be thrown if scanning of media file is not successful.
 * @author Denis Pasek, Senacor Technologies AG
 */
public class MediaFileScanException extends Exception {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    public MediaFileScanException(String msg) {
        super(msg);
    }

    public MediaFileScanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
