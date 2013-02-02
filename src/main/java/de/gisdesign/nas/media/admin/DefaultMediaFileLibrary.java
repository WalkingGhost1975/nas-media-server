package de.gisdesign.nas.media.admin;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Simple default implementation of the {@link MediaFileLibrary} interface.
 * @author Denis Pasek
 */
final class DefaultMediaFileLibrary implements MediaFileLibrary {

    private List<File> rootDirectories;

    DefaultMediaFileLibrary(List<File> rootDirectories) {
        this.rootDirectories = rootDirectories;
    }

    @Override
    public List<File> getRootDirectories() {
        return Collections.unmodifiableList(rootDirectories);
    }

}
