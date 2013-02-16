package de.gisdesign.nas.media.rest.library;

import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author Denis Pasek
 */
@JsonTypeName("MediaLibraryLocation")
public class MediaLibraryLocationDTO {

    private String name;

    private String uri;

    public MediaLibraryLocationDTO(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
