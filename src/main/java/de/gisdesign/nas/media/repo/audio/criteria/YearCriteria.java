package de.gisdesign.nas.media.repo.audio.criteria;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.CatalogMetaDataQueryBuilderTemplate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

/**
 * A single value criteria used to filter for the release year of the audio file.
 * @author Denis Pasek
 */
@Component("audio:year")
public final class YearCriteria extends CatalogMetaDataQueryBuilderTemplate {

    public YearCriteria() {
        super(MediaFileType.AUDIO, "year");
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