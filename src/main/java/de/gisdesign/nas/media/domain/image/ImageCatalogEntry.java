package de.gisdesign.nas.media.domain.image;

import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.domain.catalog.MediaFileCatalogEntry;
import java.io.File;
import java.security.Principal;
import java.util.List;

/**
 * A {@link CatalogEntry} representing an image file.
 * @author Denis Pasek
 */
public class ImageCatalogEntry implements MediaFileCatalogEntry {

    /**
     * The parent {@link CatalogEntry}.
     */
    private CatalogEntry parent;

    /**
     * The referenced image {@link File}.
     */
    private File imageFile;

    /**
     * The basic image meta data.
     */
    private ImageFileData imageData;

    /**
     * The path in the navigation structure.
     */
    private String path;

    /**
     * Constructor.
     * @param parent
     * @param imageData
     */
    public ImageCatalogEntry(CatalogEntry parent, ImageFileData imageData) {
        this.parent = parent;
        this.imageData = imageData;
        this.imageFile = new File(imageData.getAbsolutePath(), imageData.getFilename());
        this.path = (parent != null) ? parent.getPath() : "/";
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return imageFile.getName();
    }

    public ImageFileData getImageData() {
        return imageData;
    }

    @Override
    public CatalogEntry getParent() {
        return parent;
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public List<CatalogEntry> getChildren() {
        return null;
    }

    @Override
    public boolean hasChild(String name) {
        return false;
    }

    @Override
    public CatalogEntry getChild(String name) {
        return null;
    }

    @Override
    public boolean isAuthorized(Principal principal) {
        return true;
    }

    @Override
    public long getLastModified() {
        return imageFile.lastModified();
    }

    @Override
    public long getSize() {
        return imageFile.length();
    }

    @Override
    public String getPath() {
        return path;
    }

}
