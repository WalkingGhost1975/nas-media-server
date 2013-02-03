package de.gisdesign.nas.media.rest;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import javax.ws.rs.core.UriInfo;

/**
 * Interface describing a strategy interface used to build {@link MediaFile}s and sub resource
 * of a specific type.
 * @param <M> The supported {@link MediaFileData} type.
 * @author Denis Pasek
 */
public interface CatalogEntryResourceBuilder<M extends MediaFileData> {

    /**
     * Must create a specific {@link MediaFile} DTO for
     * the {@link CatalogEntry} representing a media file.
     * @param catalogEntry The {@link CatalogEntry} representing a media file.
     * @param uriInfo The REST {@link UriInfo} of this resource being the parent of the sub resource to be created.
     * @return The {@link MediaFile} for this media file.
     */
    public MediaFileDTO buildMediaFile(CatalogEntry catalogEntry, UriInfo uriInfo);

    /**
     * Must create a REST sub resource representing
     * the specified {@link CatalogEntry} that represents media file.
     * @param catalogEntry The {@link CatalogEntry} representing a media file.
     * @param uriInfo The REST {@link UriInfo} of this resource being the parent of the sub resource to be created.
     * @return The created sub resource for the media file.
     */
    public Object buildMediaFileResource(CatalogEntry catalogEntry, UriInfo uriInfo);

}
