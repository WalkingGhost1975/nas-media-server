package de.gisdesign.nas.media.repo.audio;

import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.repo.DiscreteValueListSource;
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
 * @author Denis Pasek
 */
@Repository
class AudioRepositoryDAOImpl implements AudioRepositoryDAO {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MetaDataQueryBuilderRegistry queryBuilderRegistry;

    @Override
    public List<AudioFileData> findAudioFilesByDirectory(String directoryName) {
        TypedQuery<AudioFileData> query = em.createQuery("SELECT afd from AudioFileData afd WHERE afd.absolutePath=?1", AudioFileData.class);
        query.setParameter(1, directoryName);
        return query.getResultList();
    }

    @Override
    public AudioFileData findAudioFileById(Long id) {
        return em.find(AudioFileData.class, id);
    }

    @Override
    public AudioFileData findAudioFileByAbsoluteFileName(String absoluteFileName) {
        File file = new File(absoluteFileName);
        TypedQuery<AudioFileData> query = em.createQuery("SELECT afd from AudioFileData afd WHERE afd.absolutePath=?1 AND afd.filename=?2", AudioFileData.class);
        query.setParameter(1, file.getParent());
        query.setParameter(2, file.getName());
        return query.getSingleResult();
    }

    @Transactional
    @Override
    public AudioFileData saveAudioFile(AudioFileData audioFileData) {
        AudioFileData savedData = audioFileData;
        if (audioFileData.getId() != null)  {
            savedData = em.merge(audioFileData);
        } else {
            em.persist(audioFileData);
        }
        return savedData;
    }

    @Transactional
    @Override
    public void deleteAudioFile(AudioFileData mediaFileData) {
        AudioFileData audioFileData = em.merge(mediaFileData);
        em.remove(audioFileData);
    }

    @Override
    public List<AudioFileData> findAudioFilesByCriteria(final MetaDataCriteria<?> criteria) {
        //Prepare Query
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AudioFileData> query = cb.createQuery(AudioFileData.class);
        Root<AudioFileData> root = query.from(AudioFileData.class);
        query.select(root);
        Predicate filter = assembleQueryFilter(criteria, cb, root);
        return em.createQuery(query.where(filter)).getResultList();
    }

    @Override
    @SuppressWarnings({"rawtypes","unchecked"})
    public List<String> loadAudioFileCriteriaValues(MetaDataCriteria<?> criteria) {
        DiscreteValueListSource valueListSource = this.queryBuilderRegistry.getDiscreteValueListSource(criteria.getId());
        //Prepare Query
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery();
        Root<AudioFileData> root = query.from(AudioFileData.class);
        query.select(valueListSource.buildExpression(cb, root));
        //Traverse parent criteria hierarchy
        MetaDataCriteria<?> currentCriteria = criteria.getParent();
        Predicate filter = assembleQueryFilter(currentCriteria, cb, root);
        //Assemble query
        if (filter != null)  {
            query.where(filter);
        }
        query.groupBy(valueListSource.buildExpression(cb, root));
        //Execute query
        List<Object> criteriaValues = em.createQuery(query).getResultList();
        return valueListSource.convertCriteriaValues(criteriaValues);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    private Predicate assembleQueryFilter(final MetaDataCriteria<?> criteria, CriteriaBuilder cb, Root<AudioFileData> root) {
        //Traverse criteria hierarchy
        MetaDataCriteria<?> currentCriteria = criteria;
        Predicate filter = null;
        while (currentCriteria != null)  {
            MetaDataQueryBuilder queryCriteria = this.queryBuilderRegistry.getQueryBuilder(currentCriteria.getId());
            Predicate filterPredicate = queryCriteria.buildPredicate(cb, root, currentCriteria.getValue());
            filter = (filter == null) ? filterPredicate : cb.and(filter,filterPredicate);
            currentCriteria = currentCriteria.getParent();
        }
        return filter;
    }
}
