package de.gisdesign.nas.media.domain.image;

import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.domain.catalog.DefaultMediaFileCatalogEntry;
import java.io.File;

/**
 * A {@link CatalogEntry} representing an image file.
 * @author Denis Pasek
 */
public class ImageCatalogEntry extends DefaultMediaFileCatalogEntry {

    /**
     * The basic image meta data.
     */
    private ImageFileData imageData;

    /**
     * Constructor.
     * @param parent
     * @param imageData
     */
    public ImageCatalogEntry(CatalogEntry parent, ImageFileData imageData) {
        super(imageData.getId(), new File(imageData.getAbsolutePath(), imageData.getFilename()), parent);
        this.imageData = imageData;
    }

    public ImageFileData getImageData() {
        return imageData;
    }
}
