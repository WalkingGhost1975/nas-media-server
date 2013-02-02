package de.gisdesign.nas.media.repo.image;

import java.io.File;

/**
 *
 * @author Denis Pasek
 */
public class ScaledImageResources {

    private File slideShowImage;

    private File thumbnailBig;

    private File thumbnailSmall;

    ScaledImageResources(File slideShowFile, File thumbnailBigFile, File thumbnailSmallFile) {
        this.slideShowImage = slideShowFile;
        this.thumbnailBig = thumbnailBigFile;
        this.thumbnailSmall = thumbnailSmallFile;
    }

    public File getSlideShowImage() {
        return slideShowImage;
    }

    public File getThumbnailBig() {
        return thumbnailBig;
    }

    public File getThumbnailSmall() {
        return thumbnailSmall;
    }

}
