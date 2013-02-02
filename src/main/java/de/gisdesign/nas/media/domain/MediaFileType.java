package de.gisdesign.nas.media.domain;

/**
 * Enumeration containing the supported media file types.
 * @author Denis Pasek
 */
public enum MediaFileType {

    IMAGE("image"),

    AUDIO("audio"),

    VIDEO("video");

    private String namespace;

    private MediaFileType(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }
}
