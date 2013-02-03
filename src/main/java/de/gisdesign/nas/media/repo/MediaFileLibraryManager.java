package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import java.util.List;

/**
 * Interface for the management of {@link MediaFileLibrary}s.
 * @author Denis Pasek
 */
public interface MediaFileLibraryManager {

    /**
     * Retrieves the names of all {@link MediaFileLibrary}s of the specified {@link MediaFileType}.
     * @param mediaType The requested {@link MediaFileType}.
     * @return The names of the {@link MediaFileLibrary}s.
     */
    public List<String> getMediaFileLibraryNames(MediaFileType mediaType);


    /**
     * Create a new {@link MediaFileLibrary} with the specified name for the given {@link MediaFileType}.
     * @param mediaType The requested {@link MediaFileType}.
     * @param libraryName The libraryName of the {@link MediaFileLibrary}.
     * @return The newly created {@link MediaFileLibrary}.
     */
    public MediaFileLibrary createMediaFileLibrary(MediaFileType mediaType, String libraryName);

    /**
     * Delets a {@link MediaFileLibrary} with the specified name for the given {@link MediaFileType}.
     * @param mediaType The requested {@link MediaFileType}.
     * @param libraryName The libraryName of the {@link MediaFileLibrary}.
     */
    public void deleteMediaFileLibrary(MediaFileType mediaType, String libraryName);

    /**
     * Loads the {@link MediaFileLibrary} with the specified name and {@link MediaFileType}.
     * @param mediaType The requested {@link MediaFileType}.
     * @param libraryName The libraryName of the {@link MediaFileLibrary}.
     * @return The {@link MediaFileLibrary}.
     */
    public MediaFileLibrary getMediaFileLibrary(MediaFileType mediaType, String libraryName);

    /**
     * Adds a root directory to the {@link MediaFileLibrary} of the specified {@link MediaFileType}.
     * @param mediaType The {@link MediaFileType}.
     * @param libraryName The name of the {@link MediaFileLibrary}.
     * @param directoryName The unique name of the root directory. Must be unique in the scope of the {@link MediaFileType}.
     * @param path The path of the root directory on the local hard disk.
     * @throws InvalidRootDirectoryException The specified path is not a valid directory in the filesystem.
     */
    public void addMediaFileLibraryDirectory(MediaFileType mediaType, String libraryName, String directoryName, String path) throws InvalidRootDirectoryException;

    /**
     * Removes a root directory to the {@link MediaFileLibrary} of the specified {@link MediaFileType}.
     * @param mediaType The {@link MediaFileType}.
     * @param libraryName The name of the {@link MediaFileLibrary}.
     * @param directoryName The unique name of the root directory to be removed.
     * @return <code>true</code> if root directory has been removed.
     */
    public boolean removeMediaFileLibraryDirectory(MediaFileType mediaType, String libraryName, String directoryName);


}
