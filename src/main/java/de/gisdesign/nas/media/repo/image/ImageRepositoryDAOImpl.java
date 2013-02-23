package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.AbstractMediaFileDAO;
import de.gisdesign.nas.media.repo.MediaFileDataDAO;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

/**
 * {@link MediaFileDataDAO} implementation for {@link ImageFileData}. Is based on
 * the {@link AbstractMediaFileDAO} and implements the additional methods defined in
 * {@link ImageRepositoryDAO}.
 * @author Denis Pasek
 */
@Repository
public class ImageRepositoryDAOImpl extends AbstractMediaFileDAO<ImageFileData> implements ImageRepositoryDAO {

    public ImageRepositoryDAOImpl() {
        super(ImageFileData.class);
    }

    @Override
    public List<ImageFileData> findImagesForRescaling(int index, int batchSize) {
        TypedQuery<ImageFileData> query = getEntityManager().createQuery("SELECT ifd FROM ImageFileData ifd ORDER BY ifd.absolutePath, ifd.filename", ImageFileData.class);
        query.setFirstResult(index);
        query.setMaxResults(batchSize);
        return query.getResultList();
    }

}
