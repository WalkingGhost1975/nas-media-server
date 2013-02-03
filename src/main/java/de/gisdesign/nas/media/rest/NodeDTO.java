package de.gisdesign.nas.media.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 *
 * @author Denis Pasek
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME)
public abstract class NodeDTO {

    private String name;

    private String uri;

    public NodeDTO() {
    }

    public NodeDTO(String name, String uri) {
        Validate.notNull(name, "Name is null.");
        Validate.notNull(uri, "URI is null.");
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
