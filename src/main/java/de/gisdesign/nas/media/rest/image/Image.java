package de.gisdesign.nas.media.rest.image;

import de.gisdesign.nas.media.rest.MediaFile;
import java.util.List;

/**
 * REST service DTO for Image resources.
 * @author Denis PasekS
 */
public class Image extends MediaFile {

    private String thumbSmallUri;

    private String thumbBigUri;

    private String slideShowUri;

    private List<String> tags;

    public Image() {
    }

    public Image(String name, String uri) {
        super(name, uri);
    }

    public Image(String name, String uri, long lastModified, long size) {
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
