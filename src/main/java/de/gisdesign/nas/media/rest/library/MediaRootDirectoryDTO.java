package de.gisdesign.nas.media.rest.library;

import de.gisdesign.nas.media.domain.MediaRootDirectory;

/**
 * REST DTO representing a {@link MediaRootDirectory}.
 * @author Denis Pasek
 */
public class MediaRootDirectoryDTO {

    private String name;

    private String path;

    private String uri;

    public MediaRootDirectoryDTO() {
    }

    public MediaRootDirectoryDTO(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public MediaRootDirectoryDTO(String name, String path, String uri) {
        this.name = name;
        this.path = path;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
