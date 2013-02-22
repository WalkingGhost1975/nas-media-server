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
@Criteria("audio:albumArtist")
public final class AlbumArtistCriteria extends DiscreteValueMetaDataCriteria<String>  {

    public AlbumArtistCriteria() {
        super("audio:albumArtist");
    }

    @Override
    public Expression<String> buildExpression(CriteriaBuilder cb, Root<?> root) {
        return root.get("metaData").get("albumArtist");
    }

    @Override
    protected String convertStringToValue(String stringValue) {
        return stringValue;
    }
}
