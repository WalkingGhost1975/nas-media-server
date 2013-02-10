package de.gisdesign.nas.media.rest.audio;

import de.gisdesign.nas.media.domain.audio.AudioCatalogEntry;
import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.repo.audio.AudioMediaRepository;
import de.gisdesign.nas.media.rest.CatalogEntryResourceBuilder;
import de.gisdesign.nas.media.rest.MediaFileDTO;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Denis Pasek
 */
public class AudioResourceBuilder implements CatalogEntryResourceBuilder<AudioFileData> {

    /**
     * The underlying {@link AudioMediaRepository}.
     */
    private AudioMediaRepository audioRepository;

    /**
     * Constructor.
     * @param audioRepository The udnerlying {@link AudioMediaRepository} to use.
     */
    public AudioResourceBuilder(AudioMediaRepository audioRepository) {
        Validate.notNull(audioRepository, "AudioMediaRepository is null.");
        this.audioRepository = audioRepository;
    }

    public AudioMediaRepository getAudioRepository() {
        return audioRepository;
    }

    @Override
    public MediaFileDTO buildMediaFile(CatalogEntry catalogEntry, UriInfo uriInfo) {
        Validate.notNull(catalogEntry, "CatalogEntry is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        String uri = uriInfo.getAbsolutePathBuilder().path(catalogEntry.getName()).build().toString();
        return buildMediaFile(catalogEntry, uriInfo, uri);
    }

    public MediaFileDTO buildMediaFile(CatalogEntry catalogEntry, UriInfo uriInfo, String uri) {
        Validate.notNull(catalogEntry, "CatalogEntry is null.");
        Validate.notNull(uri, "Uri is null.");
        AudioCatalogEntry audioEntry = (AudioCatalogEntry) catalogEntry;

        AudioFileData audioFileData = audioEntry.getAudioFileData();

        AudioDTO audio = new AudioDTO(catalogEntry.getName(), uri, audioEntry.getLastModified(), audioEntry.getSize());
        //Set download link
        audio.setDownloadUri(uriInfo.getAbsolutePathBuilder().path("/download").build().toString());
        //Set metadata link
        audio.setMetadataUri(uriInfo.getAbsolutePathBuilder().path("/metadata").build().toString());

        return audio;
    }

    @Override
    public Object buildMediaFileResource(CatalogEntry catalogEntry, UriInfo uriInfo) {
        Validate.notNull(catalogEntry, "CatalogEntry is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        return new AudioFileResource(this, catalogEntry, uriInfo);
    }

}
