package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.domain.image.ImageMetaData;
import de.gisdesign.nas.media.repo.MetaDataQueryBuilder;
import de.gisdesign.nas.media.repo.MetaDataQueryBuilderRegistry;
import java.io.File;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Denis Pasek, Senacor Technologies AG
 */
@Repository
class ImageRepositoryDAOImpl implements ImageRepositoryDAO {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MetaDataQueryBuilderRegistry queryBuilderRegistry;

    @Override
    public List<ImageFileData> findImagesByDirectory(String directoryName) {
        TypedQuery<ImageFileData> query = em.createQuery("SELECT ifd from ImageFileData ifd WHERE ifd.absolutePath=?1", ImageFileData.class);
        query.setParameter(1, directoryName);
        return query.getResultList();
    }

    @Override
    public ImageFileData findImageById(Long id) {
        return em.find(ImageFileData.class, id);
    }

    @Override
    public ImageFileData findImageByAbsoluteFileName(String absoluteFileName) {
        File file = new File(absoluteFileName);
        TypedQuery<ImageFileData> query = em.createQuery("SELECT ifd from ImageFileData ifd WHERE ifd.absolutePath=?1 AND ifd.filename=?2", ImageFileData.class);
        query.setParameter(1, file.getParent());
        query.setParameter(2, file.getName());
        return query.getSingleResult();
    }

    @Override
    public ImageMetaData loadImageMetaDataById(Long id) {
        return em.find(ImageMetaData.class, id);
    }

    @Transactional
    @Override
    public ImageFileData saveImage(ImageFileData imageFileData) {
        ImageFileData savedData = imageFileData;
        if (imageFileData.getId() != null)  {
            savedData = em.merge(imageFileData);
        } else {
            em.persist(imageFileData);
        }
        return savedData;
    }

    @Transactional
    @Override
    public void deleteImage(ImageFileData mediaFileData) {
        ImageFileData imageFileData = em.merge(mediaFileData);
        em.remove(imageFileData);
    }

    @Override
    public List<ImageFileData> findImagesByCriteria(final MetaDataCriteria criteria) {
        //Prepare Query
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ImageFileData> query = cb.createQuery(ImageFileData.class);
        Root<ImageFileData> root = query.from(ImageFileData.class);
        query.select(root);
        Predicate filter = assembleQueryFilter(criteria, cb, root);
        return em.createQuery(query.where(filter)).getResultList();
    }

    @Override
    public List<String> loadImageCriteriaValues(MetaDataCriteria criteria) {
        MetaDataQueryBuilder queryBuilder = this.queryBuilderRegistry.getQueryBuilder(criteria.getMediaFileType(), criteria.getName());
        //Prepare Query
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery();
        Root<ImageFileData> root = query.from(ImageFileData.class);
        query.select(queryBuilder.buildExpression(cb, root));
        //Traverse parent criteria hierarchy
        MetaDataCriteria currentCriteria = criteria.getParent();
        Predicate filter = assembleQueryFilter(currentCriteria, cb, root);
        //Assemble query
        if (filter != null)  {
            query.where(filter);
        }
        query.groupBy(queryBuilder.buildExpression(cb, root));
        //Execute query
        List<Object> criteriaValues = em.createQuery(query).getResultList();
        return queryBuilder.convertCriteriaValues(criteriaValues);
    }

    private Predicate assembleQueryFilter(final MetaDataCriteria criteria, CriteriaBuilder cb, Root<ImageFileData> root) {
        //Traverse criteria hierarchy
        MetaDataCriteria currentCriteria = criteria;
        Predicate filter = null;
        while (currentCriteria != null)  {
            MetaDataQueryBuilder queryCriteria = this.queryBuilderRegistry.getQueryBuilder(currentCriteria.getMediaFileType(), currentCriteria.getName());
            Predicate filterPredicate = queryCriteria.buildPredicate(cb, root, currentCriteria.getValue());
            filter = (filter == null) ? filterPredicate : cb.and(filter,filterPredicate);
            currentCriteria = currentCriteria.getParent();
        }
        return filter;
    }

    @Override
    public List<ImageFileData> findImagesForRescaling(int index, int batchSize) {
        TypedQuery<ImageFileData> query = em.createQuery("SELECT ifd FROM ImageFileData ifd ORDER BY ifd.absolutePath, ifd.filename", ImageFileData.class);
        query.setFirstResult(index);
        query.setMaxResults(batchSize);
        return query.getResultList();
    }

}
