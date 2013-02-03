package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.rest.MediaFileDTO;
import java.util.List;

/**
 * REST service DTO for Image resources.
 * @author Denis PasekS
 */
public class ImageDTO extends MediaFileDTO {

    private String thumbSmallUri;

    private String thumbBigUri;

    private String slideShowUri;

    private List<String> tags;

    public ImageDTO() {
    }

    public ImageDTO(String name, String uri) {
        super(name, uri);
    }

    public ImageDTO(String name, String uri, long lastModified, long size) {
        super(name, uri, lastModified, size);
    }

    public String getThumbSmallUri() {
        return thumbSmallUri;
    }

    public void setThumbSmallUri(String thumbSmallUri) {
        this.thumbSmallUri = thumbSmallUri;
    }

    public String getThumbBigUri() {
        return thumbBigUri;
    }

    public void setThumbBigUri(String thumbBigUri) {
        this.thumbBigUri = thumbBigUri;
    }

    public String getSlideShowUri() {
        return slideShowUri;
    }

    public void setSlideShowUri(String slideShowUri) {
        this.slideShowUri = slideShowUri;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
