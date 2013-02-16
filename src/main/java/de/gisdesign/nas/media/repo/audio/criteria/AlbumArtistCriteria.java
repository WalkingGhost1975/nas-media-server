package de.gisdesign.nas.media.repo.audio.criteria;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.CatalogMetaDataQueryBuilderTemplate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

/**
 * A single value criteria used to filter for the genre of the audio file.
 * @author Denis Pasek
 */
@Component("audio:albumArtist")
public final class AlbumArtistCriteria extends CatalogMetaDataQueryBuilderTemplate {

    public AlbumArtistCriteria() {
        super(MediaFileType.AUDIO, "albumArtist");
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
