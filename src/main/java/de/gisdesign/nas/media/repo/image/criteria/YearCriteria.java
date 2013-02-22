package de.gisdesign.nas.media.repo.image.criteria;

import de.gisdesign.nas.media.domain.Criteria;
import de.gisdesign.nas.media.domain.DiscreteValueMetaDataCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 *
 * @author Denis Pasek
 */
@Criteria("image:creationDateYear")
public final class YearCriteria extends DiscreteValueMetaDataCriteria<Integer> {

    public YearCriteria() {
        super("image:creationDateYear");
    }

    @Override
    public Expression<Integer> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return cb.function("year", Integer.class, root.get("metaData").get("creationDate"));
    }

    @Override
    protected Integer convertStringToValue(String stringValue) {
        return Integer.parseInt(stringValue);
    }
}
