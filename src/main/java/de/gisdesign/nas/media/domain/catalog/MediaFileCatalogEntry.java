package de.gisdesign.nas.media.domain.catalog;

/**
 * Extension of the {@link CatalogEntry} interface used for leaf nodes of the
 * catalogs representing media files.
 * @author Denis Pasek
 */
public interface MediaFileCatalogEntry extends CatalogEntry {

    /**
     * Returns the last modification date of this {@link CatalogEntry}.
     * @return The last modification timestamp.
     */
    public long getLastModified();

    /**
     * Returns the size in bytes of this media file.
     * @return
     */
    public long getSize();

}
