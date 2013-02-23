package de.gisdesign.nas.media.domain.catalog;

import java.io.File;
import java.security.Principal;
import java.util.List;

/**
 *
 * @author Denis Pasek
 */
public abstract class DefaultMediaFileCatalogEntry implements MediaFileCatalogEntry {
    /**
     * The referenced audio {@link File}.
     */
    private Long id;
    /**
     * The referenced audio {@link File}.
     */
    private File mediaFile;
    /**
     * The parent {@link CatalogEntry}.
     */
    private CatalogEntry parent;
    /**
     * The path in the navigation structure.
     */
    private String path;

    public DefaultMediaFileCatalogEntry(Long id, File mediaFile, CatalogEntry parent) {
        this.id = id;
        this.mediaFile = mediaFile;
        this.parent = parent;
        this.path = (parent != null) ? parent.getPath() : "/";
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public CatalogEntry getChild(String name) {
        return null;
    }

    @Override
    public List<CatalogEntry> getChildren() {
        return null;
    }

    @Override
    public long getLastModified() {
        return mediaFile.lastModified();
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public CatalogEntry getParent() {
        return parent;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public long getSize() {
        return mediaFile.length();
    }

    @Override
    public boolean hasChild(String name) {
        return false;
    }

    @Override
    public boolean isAuthorized(Principal principal) {
        return true;
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    public long size() {
        return 0;
    }

}
