package de.gisdesign.nas.media.repo.image.criteria;

import de.gisdesign.nas.media.repo.DiscreteValueMetaDataQueryBuilderTemplate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek
 */
@Component("image:creationDateYear")
public final class YearCriteria extends DiscreteValueMetaDataQueryBuilderTemplate<Integer> {

    @Override
    public Expression<Integer> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return cb.function("year", Integer.class, root.get("metaData").get("creationDate"));
    }

    @Override
    protected Integer convertStringToValue(String stringValue) {
        return Integer.parseInt(stringValue);
    }
}
