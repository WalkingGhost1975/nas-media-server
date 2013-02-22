package de.gisdesign.nas.media.repo.audio.criteria;

import de.gisdesign.nas.media.domain.Criteria;
import de.gisdesign.nas.media.domain.DiscreteValueMetaDataCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 * A single value criteria used to filter for the release year of the audio file.
 * @author Denis Pasek
 */
@Criteria("audio:year")
public final class YearCriteria extends DiscreteValueMetaDataCriteria<Integer> {

    public YearCriteria() {
        super("audio:year");
    }

    @Override
    public Expression<Integer> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return root.get("metaData").get("year");
    }

    @Override
    protected Integer convertStringToValue(String stringValue) {
        return Integer.parseInt(stringValue);
    }
}
