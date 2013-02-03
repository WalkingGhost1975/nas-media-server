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
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.Validate;

/**
 * Entity use for storing configuration parameters for the media repositories
 * in the data base
 * @author Denis Pasek
 */
@Entity
@Table(name="CONFIGURATION", uniqueConstraints={
    @UniqueConstraint(columnNames={"MEDIA_TYPE", "PARAM_NAME"})
})
public class ConfigurationParameter implements Serializable {
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

    public ConfigurationParameter() {
    }

    public ConfigurationParameter(MediaFileType mediaFileType, String name, String value) {
        this.mediaFileType = mediaFileType;
        this.name = name;
        this.value = value;
    }

    public MediaFileType getMediaFileType() {
        return mediaFileType;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void updateValue(String value)  {
        Validate.notNull(value, "New value of configuration parameter is null.");
        this.value = value;
    }
}
