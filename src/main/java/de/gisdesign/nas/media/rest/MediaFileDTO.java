package de.gisdesign.nas.media.rest;

/**
 *
 * @author Denis Pasek
 */
public class MediaFileDTO extends NodeDTO {

    private Long lastModified;

    private Long size;

    private String metadataUri;

    private String downloadUri;

    public MediaFileDTO() {
    }

    public MediaFileDTO(String name, String uri) {
        super(name, uri);
    }

    public MediaFileDTO(String name, String uri, Long lastModified, Long size) {
        super(name, uri);
        this.lastModified = lastModified;
        this.size = size;
        this.downloadUri = uri + "/download";
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
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
