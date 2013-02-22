package de.gisdesign.nas.media.repo.image.criteria;

import de.gisdesign.nas.media.domain.Criteria;
import de.gisdesign.nas.media.domain.DiscreteValueMetaDataCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek
 */
@Criteria("image:tags")
public final class TagCriteria extends DiscreteValueMetaDataCriteria<String> {

    public TagCriteria() {
        super("image:tags");
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
