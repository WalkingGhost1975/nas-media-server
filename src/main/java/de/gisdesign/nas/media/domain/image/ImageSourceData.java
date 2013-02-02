package de.gisdesign.nas.media.domain.image;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Denis Pasek
 */
@Embeddable
public class ImageSourceData implements Serializable {

    @Column(name="AUTHOR")
    private String author;

    @Column(name="COPYRIGHT")
    private String copyright;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

}
