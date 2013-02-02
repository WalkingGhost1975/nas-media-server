package de.gisdesign.nas.media.admin;

import de.gisdesign.nas.media.domain.MediaFileType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Denis Pasek
 */
@Entity
@Table(name="CONFIG_PARAMS")
public class MediaRepositoryConfigurationParameter implements Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="MEDIA_TYPE")
    private MediaFileType mediaFileType;

    @Column(name="PARAM_NAME")
    private String name;

    @Column(name="PARAM_VALUE")
    private String value;

    public Long getId() {
        return id;
    }

    public MediaFileType getMediaFileType() {
        return mediaFileType;
    }

    public void setMediaFileType(MediaFileType mediaFileType) {
        this.mediaFileType = mediaFileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
