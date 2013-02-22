package de.gisdesign.nas.media.repo.audio.criteria;

import de.gisdesign.nas.media.domain.Criteria;
import de.gisdesign.nas.media.domain.ValueListMetaDataCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 * A value list criteria used to filter for the genre of the audio file.
 * @author Denis Pasek
 */
@Criteria("audio:genre:list")
public final class GenreListCriteria extends ValueListMetaDataCriteria<String> {

    public GenreListCriteria() {
        super("audio:genre:list");
    }

    @Override
    public Expression<String> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return root.get("metaData").get("genre");
    }

    @Override
    public String getValueAsString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValueAsString(String stringValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
