package de.gisdesign.nas.media.rest.audio;

import de.gisdesign.nas.media.rest.MediaFileDTO;

/**
 * REST service DTO for Audio File resources.
 * @author Denis PasekS
 */
public class AudioDTO extends MediaFileDTO {

    public AudioDTO() {
    }

    public AudioDTO(String name, String uri) {
        super(name, uri);
    }

    public AudioDTO(String name, String uri, long lastModified, long size) {
        super(name, uri, lastModified, size);
    }
}
