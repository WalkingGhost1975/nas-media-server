package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.SingleValueMetaDataQueryBuilderTemplate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek
 */
@Component
public final class ImageYearCriteria extends SingleValueMetaDataQueryBuilderTemplate {

    public ImageYearCriteria() {
        super(MediaFileType.IMAGE, "creationDateYear");
    }

    @Override
    public Expression<?> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return cb.function("year", Integer.class, root.get("metaData").get("creationDate"));
    }

    @Override
    protected String convertValueToString(Object criteriaValue) {
        return String.valueOf(criteriaValue);
    }

    @Override
    protected Object convertStringToValue(String stringValue) {
        return Integer.parseInt(stringValue);
    }
}
