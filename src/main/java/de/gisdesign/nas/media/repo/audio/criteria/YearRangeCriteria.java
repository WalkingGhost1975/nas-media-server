package de.gisdesign.nas.media.repo.audio.criteria;

import de.gisdesign.nas.media.repo.ValueRangeMetaDataQueryBuilderTemplate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek
 */
@Component("audio:yearRange")
public class YearRangeCriteria extends ValueRangeMetaDataQueryBuilderTemplate<Integer> {

    @Override
    public Expression<Integer> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return root.get("metaData").get("year");
    }

    @Override
    protected Integer convertStringToValue(String stringValue) {
        return Integer.parseInt(stringValue);
    }

}
