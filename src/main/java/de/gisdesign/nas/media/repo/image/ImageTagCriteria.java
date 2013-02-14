package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.SingleValueMetaDataQueryBuilderTemplate;
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
 * @author Denis Pasek
 */
@Component
public final class ImageTagCriteria extends SingleValueMetaDataQueryBuilderTemplate {

    public ImageTagCriteria() {
        super(MediaFileType.IMAGE, "tags");
    }

    @Override
    public Expression<?> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return root.join("tags").get("text");
    }

    @Override
    protected String convertValueToString(Object criteriaValue) {
        return String.valueOf(criteriaValue);
    }

    @Override
    protected Object convertStringToValue(String stringValue) {
        return stringValue;
    }

}
