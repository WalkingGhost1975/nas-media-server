package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.MetaDataQueryBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek, Senacor Technologies AG
 */
@Component
class ImageYearCriteria implements MetaDataQueryBuilder {

    @Override
    public MediaFileType getMediaFileType() {
        return MediaFileType.IMAGE;
    }

    @Override
    public String getName() {
        return "creationDateYear";
    }

    @Override
    public List<String> convertCriteriaValues(List<Object> criteriaObjects) {
        List<String> criteriaValues = new ArrayList<String>(criteriaObjects.size());
        for (Object value : criteriaObjects) {
            if (value != null)  {
                criteriaValues.add(String.valueOf(value));
            } else {
                criteriaValues.add(UNKNOWN_VALUE);
            }
        }
        return criteriaValues;
    }

    @Override
    public Predicate buildPredicate(CriteriaBuilder cb, Root<?> root, String criteriaValue) {
        Predicate predicate;
        if (UNKNOWN_VALUE.equals(criteriaValue) || criteriaValue == null)  {
            predicate = cb.isNull(buildExpression(cb, root));
        } else {
            predicate = cb.equal(buildExpression(cb, root), Integer.parseInt(criteriaValue));
        }
        return predicate;
    }

    @Override
    public Expression<?> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return cb.function("year", Integer.class, root.get("metaData").get("creationDate"));
    }

}
