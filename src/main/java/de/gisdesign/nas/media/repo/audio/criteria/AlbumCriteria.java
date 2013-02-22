package de.gisdesign.nas.media.repo.audio.criteria;

import de.gisdesign.nas.media.domain.Criteria;
import de.gisdesign.nas.media.domain.DiscreteValueMetaDataCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 * A single value criteria used to filter for the genre of the audio file.
 * @author Denis Pasek
 */
@Criteria("audio:album")
public final class AlbumCriteria extends DiscreteValueMetaDataCriteria<String> {

    public AlbumCriteria() {
        super("audio:album");
    }

    @Override
    public Expression<String> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return root.get("metaData").get("album");
    }

    @Override
    protected String convertStringToValue(String stringValue) {
        return stringValue;
    }
}
