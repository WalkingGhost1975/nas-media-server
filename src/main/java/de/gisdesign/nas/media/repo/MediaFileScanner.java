package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;

/**
 *
 * @author Denis Pasek
 */
public interface MediaFileScanner {

    /**
     * Returns the supported {@link MediaFileType} of the {@link MediaFileScanner}. Used for registration
     * of scanners. Only one scanner for a specific {@link MediaFileType} cannot be registered at a time.
     * @return The {@link MediaFileType}.
     */
    public MediaFileType getMediaFileType();

    /**
     * Scans the directory structures managed by the specified {@link MediaFileLibrary} for
     * media files which are spported for the {@link MediaFileType} of this {@link MediaFileScanner}.
     * Scanning will fully synchronize the backing database containing the media file metadata with
     * the media files found in the {@link MediaFileLibrary}.
     * @param mediaFileLibrary The {@link MediaFileLibrary} to scan.
     */
    public void scanMediaFileLibrary(MediaFileLibrary mediaFileLibrary);
}
