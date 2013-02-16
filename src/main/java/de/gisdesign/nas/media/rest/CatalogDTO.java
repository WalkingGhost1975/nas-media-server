package de.gisdesign.nas.media.rest;

import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author Denis Pasek
 */
@JsonTypeName("Catalog")
public class CatalogDTO extends NodeDTO {

    public CatalogDTO() {
    }

    public CatalogDTO(String name, String uri) {
        super(name, uri);
    }

}
