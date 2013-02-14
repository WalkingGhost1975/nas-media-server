package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.SingleValueMetaDataQueryBuilderTemplate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek
 */
@Component
public final class ImageMonthCriteria extends SingleValueMetaDataQueryBuilderTemplate {

    public ImageMonthCriteria() {
        super(MediaFileType.IMAGE, "creationDateMonth");
    }

    @Override
    public Expression<?> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return cb.function("month", Integer.class, root.get("metaData").get("creationDate"));
    }

    @Override
    protected String convertValueToString(Object criteriaValue) {
        return StringUtils.leftPad(String.valueOf(criteriaValue), 2, '0');
    }

    @Override
    protected Object convertStringToValue(String stringValue) {
        return Integer.parseInt(stringValue);
    }
}
