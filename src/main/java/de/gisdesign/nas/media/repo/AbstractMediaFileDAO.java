package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import java.io.File;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Abstract implementation of {@link MediaFileDataDAO} interface.
 * @param <M> The suported type of {@link MediaFileData}.
 * @author Denis Pasek
 */
public class AbstractMediaFileDAO<M extends MediaFileData> implements MediaFileDataDAO<M> {

    @PersistenceContext
    private EntityManager em;

    /**
     * The entity type for the supported {@link MediaFileData}.
     */
    private Class<M> mediaFileDataType;

    public AbstractMediaFileDAO(Class<M> mediaFileDataType) {
        this.mediaFileDataType = mediaFileDataType;
    }

    @Override
    public M findMediaFileById(Long id) {
        return em.find(mediaFileDataType, id);
    }

    @Override
    public M findMediaFileByAbsoluteFileName(String absoluteFileName) {
        File mediaFile = new File(absoluteFileName);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<M> query = cb.createQuery(mediaFileDataType);
        Root<M> root = query.from(mediaFileDataType);
        query.select(root);
        Predicate pathClause = cb.equal(root.get("absolutePath"), mediaFile.getParent());
        Predicate nameClause = cb.equal(root.get("filename"), mediaFile.getName());
        query.where(pathClause, nameClause);
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public List<M> findMediaFilesByDirectory(String directoryName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<M> query = cb.createQuery(mediaFileDataType);
        Root<M> root = query.from(mediaFileDataType);
        query.select(root);
        Predicate pathClause = cb.equal(root.get("absolutePath"), directoryName);
        query.where(pathClause);
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<M> findMediaFilesByCriteria(MetaDataCriteria<?> criteria) {
        //Prepare Query
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<M> query = cb.createQuery(mediaFileDataType);
        Root<M> root = query.from(mediaFileDataType);
        query.select(root);
        Predicate filter = assembleQueryFilter(criteria, cb, root);
        return em.createQuery(query.where(filter)).getResultList();
    }

    @Override
    public long countMediaFilesMatchingCriteria(MetaDataCriteria<?> metaDataCriteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<M> root = query.from(mediaFileDataType);
        query.select(cb.count(root));
        Predicate filter = assembleQueryFilter(metaDataCriteria, cb, root);
        return em.createQuery(query.where(filter)).getSingleResult();
    }

    @Override
    public <T> List<T> loadMediaFileCriteriaValues(MetaDataCriteria<T> criteria) {
        //Prepare Query
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = (CriteriaQuery<T>) cb.createQuery();
        Root<M> root = query.from(mediaFileDataType);
        query.select(criteria.buildExpression(cb, root));
        //Traverse parent criteria hierarchy
        MetaDataCriteria<?> currentCriteria = criteria.getParent();
        Predicate filter = assembleQueryFilter(currentCriteria, cb, root);
        //Assemble query
        if (filter != null)  {
            query.where(filter);
        }
        query.groupBy(criteria.buildExpression(cb, root));
        //Execute query
        return em.createQuery(query).getResultList();
    }

    @Override
    public M saveMediaFile(M mediaFileData) {
        M savedData = mediaFileData;
        if (mediaFileData.getId() != null)  {
            savedData = em.merge(mediaFileData);
        } else {
            em.persist(mediaFileData);
        }
        return savedData;
    }

    @Override
    public void deleteMediaFile(M mediaFileData) {
        M mergedMediaFileData = em.merge(mediaFileData);
        em.remove(mergedMediaFileData);
    }

    /**
     * Returns the JPA {@link EntityManager}.
     * @return The {@link EntityManager}.
     */
    protected final EntityManager getEntityManager() {
        return em;
    }

    /**
     * Assembles the {@link Predicate}s for a {@link CriteriaQuery} based on the hierarchy
     * of {@link MetaDataCriteria}.
     * @param criteria The hierarchy of {@link MetaDataCriteria}.
     * @param cb The JPA {@link CriteriaBuilder}.
     * @param root The {@link Root} expression of the query.
     * @return The {@link Predicate}.
     */
    private Predicate assembleQueryFilter(final MetaDataCriteria<?> criteria, CriteriaBuilder cb, Root<M> root) {
        //Traverse criteria hierarchy
        MetaDataCriteria<?> currentCriteria = criteria;
        Predicate filter = null;
        while (currentCriteria != null)  {
            Predicate filterPredicate = currentCriteria.buildPredicate(cb, root);
            filter = (filter == null) ? filterPredicate : cb.and(filter,filterPredicate);
            currentCriteria = currentCriteria.getParent();
        }
        return filter;
    }

}
