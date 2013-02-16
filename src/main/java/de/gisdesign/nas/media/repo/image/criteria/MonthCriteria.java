package de.gisdesign.nas.media.repo.image.criteria;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.CatalogMetaDataQueryBuilderTemplate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek
 */
@Component("image:creationDateMonth")
public final class MonthCriteria extends CatalogMetaDataQueryBuilderTemplate {

    public MonthCriteria() {
        super(MediaFileType.IMAGE, "creationDateMonth");
    }

    @Override
    public Expression<Integer> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return cb.function("month", Integer.class, root.get("metaData").get("creationDate"));
    }

    @Override
    protected String convertValueToString(Object criteriaValue) {
        return StringUtils.leftPad(String.valueOf(criteriaValue), 2, '0');
    }

    @Override
    protected Integer convertStringToValue(String stringValue) {
        return Integer.parseInt(stringValue);
    }
}
