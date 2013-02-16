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
@Component("audio:album")
public final class AlbumCriteria extends CatalogMetaDataQueryBuilderTemplate {

    public AlbumCriteria() {
        super(MediaFileType.AUDIO, "album");
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
