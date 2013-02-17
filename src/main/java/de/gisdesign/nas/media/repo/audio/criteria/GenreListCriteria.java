package de.gisdesign.nas.media.repo.audio.criteria;

import de.gisdesign.nas.media.repo.DiscreteValueListMetaDataQueryBuilderTemplate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

/**
 * A value list criteria used to filter for the genre of the audio file.
 * @author Denis Pasek
 */
@Component("audio:genre:list")
public final class GenreListCriteria extends DiscreteValueListMetaDataQueryBuilderTemplate<String> {

    @Override
    public Expression<String> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return root.get("metaData").get("genre");
    }

    @Override
    protected String convertStringToValue(String stringValue) {
        return stringValue;
    }
}
