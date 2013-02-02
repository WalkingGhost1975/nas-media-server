package de.gisdesign.nas.media.admin;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;

/**
 *
 * @author Denis Pasek
 */
public interface AdministrationService {

    /**
     * Generic method used to load a {@link MediaFileType} specific configuration parameter.
     * @param mediaFileType The {@link MediaFileType}. May not be <code>null</code>.
     * @param parameterName The name of the parameter to retrieve.
     * @return The configuration value or <code>null</code> if not yet set.
     */
    public String getConfigurationParameter(MediaFileType mediaFileType, String parameterName);

    /**
     * Generic method used to set a {@link MediaFileType} specific configuration parameter.
     * @param mediaFileType The {@link MediaFileType}. May not be <code>null</code>.
     * @param parameterName The name of the parameter to set.
     * @param parameterValue The parameter value to set. Can be any kind of string representation, even a serialized/marshalled form
     * of data up to 4k.
     */
    public void setConfigurationParameter(MediaFileType mediaFileType, String parameterName, String parameterValue);

    /**
     * Loads the {@link MediaFileLibrary} for the specified {@link MediaFileType}.
     * @param mediaType The requested {@link MediaFileType}.
     * @return The {@link MediaFileLibrary}.
     */
    public MediaFileLibrary getMediaFileLibrary(MediaFileType mediaType);

    /**
     * Adds a root directory to the {@link MediaFileLibrary} of the specified {@link MediaFileType}.
     * @param mediaType The {@link MediaFileType}.
     * @param path The path of the root directory on the local hard disk.
     * @throws InvalidRootDirectoryException The specified path is not a valid directory in the filesystem.
     */
    public void addMediaFileLibraryDirectory(MediaFileType mediaType, String path) throws InvalidRootDirectoryException;

    /**
     * Removes a root directory to the {@link MediaFileLibrary} of the specified {@link MediaFileType}.
     * @param mediaType The {@link MediaFileType}.
     * @param path The path of the root directory on the local hard disk to be removed.
     * @return <code>true</code> if root directory has been removed.
     */
    public boolean removeMediaFileLibraryDirectory(MediaFileType mediaType, String path);

}
