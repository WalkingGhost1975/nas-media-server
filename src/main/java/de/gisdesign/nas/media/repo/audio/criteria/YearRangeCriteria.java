package de.gisdesign.nas.media.repo.audio.criteria;

import de.gisdesign.nas.media.domain.Criteria;
import de.gisdesign.nas.media.domain.ValueRangeMetaDataCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 *
 * @author Denis Pasek
 */
@Criteria("audio:yearRange")
public class YearRangeCriteria extends ValueRangeMetaDataCriteria<Integer> {

    public YearRangeCriteria() {
        super("audio:yearRange");
    }

    @Override
    public Expression<Integer> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return root.get("metaData").get("year");
    }

    @Override
    public String convertToString(Integer value) {
        return String.valueOf(value);
    }
}
