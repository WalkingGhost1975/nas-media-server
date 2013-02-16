package de.gisdesign.nas.media.repo.image.criteria;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.CatalogMetaDataQueryBuilderTemplate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek
 */
@Component("image:tags")
public final class TagCriteria extends CatalogMetaDataQueryBuilderTemplate {

    public TagCriteria() {
        super(MediaFileType.IMAGE, "tags");
    }

    @Override
    public Expression<String> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return root.join("tags").get("text");
    }

    @Override
    protected String convertStringToValue(String stringValue) {
        return stringValue;
    }

}
