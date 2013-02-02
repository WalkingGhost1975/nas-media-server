package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.domain.image.ImageMetaData;
import java.util.List;

/**
 *
 * @author pasekdbh
 */
public class MockImageRepositoryDAO implements ImageRepositoryDAO {

    @Override
    public ImageFileData findImageById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageMetaData loadImageMetaDataById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageFileData findImageByAbsoluteFileName(String absoluteFileName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ImageFileData> findImagesByDirectory(String directoryName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ImageFileData> findImagesByCriteria(MetaDataCriteria criteria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ImageFileData> findImagesForRescaling(int index, int batchSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> loadImageCriteriaValues(MetaDataCriteria criteria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageFileData saveImage(ImageFileData imageFileData) {
        return imageFileData;
    }

    @Override
    public void deleteOrphanedImages(Long syncId) {
    }

}
