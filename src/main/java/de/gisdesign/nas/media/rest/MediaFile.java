package de.gisdesign.nas.media.rest;

import java.util.List;

/**
 *
 * @author Denis Pasek
 */
public class MediaFile extends Node {

    private long lastModified;

    private long size;

    private String metadataUri;

    private String downloadUri;

    public MediaFile() {
    }

    public MediaFile(String name, String uri) {
        super(name, uri);
    }

    public MediaFile(String name, String uri, long lastModified, long size) {
        super(name, uri);
        this.lastModified = lastModified;
        this.size = size;
        this.downloadUri = uri + "/download";
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getMetadataUri() {
        return metadataUri;
    }

    public void setMetadataUri(String metadataUri) {
        this.metadataUri = metadataUri;
    }

}
