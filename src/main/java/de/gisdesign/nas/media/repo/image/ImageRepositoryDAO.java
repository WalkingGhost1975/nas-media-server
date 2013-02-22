package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.domain.image.ImageMetaData;
import java.util.List;

/**
 *
 * @author Denis Pasek
 */
interface ImageRepositoryDAO {

    public ImageFileData findImageById(Long id);

    public ImageMetaData loadImageMetaDataById(Long id);

    public ImageFileData findImageByAbsoluteFileName(String absoluteFileName);

    public List<ImageFileData> findImagesByDirectory(String directoryName);

    public List<ImageFileData> findImagesByCriteria(MetaDataCriteria<?> criteria);

    public List<ImageFileData> findImagesForRescaling(int index, int batchSize);

    public <T> List<T> loadImageCriteriaValues(MetaDataCriteria<T> criteria);

    public ImageFileData saveImage(ImageFileData imageFileData);

    public void deleteImage(ImageFileData mediaFileData);

}
