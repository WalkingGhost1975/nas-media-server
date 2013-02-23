package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.MediaFileDataDAO;
import java.util.List;

/**
 * Interface for the {@link ImageMediaRepository} specific {@link MediaFileDataDAO}.
 * Adds some additional methods used by the {@link ImageMediaRepository} for managing
 * the {@link ImageFileData} in the persistent store.
 * @author Denis Pasek
 */
interface ImageRepositoryDAO extends MediaFileDataDAO<ImageFileData> {

    /**
     * Loads a batch of {@link ImageFileData} for creation of the rescaled
     * thumb nails and slide show images.
     * @param index The starting index.
     * @param batchSize The number of {@link ImageFileData} to load per call.
     * @return The list of {@link ImageFileData}.
     */
    public List<ImageFileData> findImagesForRescaling(int index, int batchSize);

}
