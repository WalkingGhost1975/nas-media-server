package de.gisdesign.nas.media.rest.library;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import java.util.List;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * REST DTO representing a {@link MediaFileLibrary}.
 * @author Denis Pasek
 */
@JsonTypeName("MediaLibrary")
public class MediaLibraryDTO {

    private String name;

    private List<MediaRootDirectoryDTO> rootDirectories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MediaRootDirectoryDTO> getRootDirectories() {
        return rootDirectories;
    }

    public void setRootDirectories(List<MediaRootDirectoryDTO> rootDirectories) {
        this.rootDirectories = rootDirectories;
    }
}
