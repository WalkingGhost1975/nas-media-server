package de.gisdesign.nas.media.repo.image.criteria;

import de.gisdesign.nas.media.domain.Criteria;
import de.gisdesign.nas.media.domain.DiscreteValueMetaDataCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Denis Pasek
 */
@Criteria("image:creationDateMonth")
public final class MonthCriteria extends DiscreteValueMetaDataCriteria<Integer> {

    public MonthCriteria() {
        super("image:creationDateMonth");
    }

    @Override
    public Expression<Integer> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return cb.function("month", Integer.class, root.get("metaData").get("creationDate"));
    }

    @Override
    protected Integer convertStringToValue(String stringValue) {
        return Integer.parseInt(stringValue);
    }

    @Override
    public String convertToString(Integer value) {
        return (value != null) ? StringUtils.leftPad(String.valueOf(value), 2, '0') : UNKNOWN_VALUE;
    }
}
