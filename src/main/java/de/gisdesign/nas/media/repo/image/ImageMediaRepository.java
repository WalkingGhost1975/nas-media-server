package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.MediaRepository;

/**
 *
 * @author Denis Pasek
 */
public interface ImageMediaRepository extends MediaRepository<ImageFileData> {

    /**
     * Generates the necessary reclaed versions of the managed image files for
     * slide shows and thumb nails.
     */
    public void generateRescaledImageFiles();

    /**
     * Retrieves the file names of the scaled versions of the image file based
     * on the currently configured file sizes. Automatically checks if the scaled versions
     * of the images are present. If not present the resource name will be left blank.
     * @param imageFileData The {@link ImageFileData} to retrieve the scaled resources for.
     * @return The scaled resourcesof the image.
     */
    public ScaledImageResources getScaledImageResources(ImageFileData imageFileData);
}
