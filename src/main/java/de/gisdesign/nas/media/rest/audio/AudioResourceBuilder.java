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
        AudioCatalogEntry audioEntry = (AudioCatalogEntry) catalogEntry;
        String audioData = String.valueOf(audioEntry.getAudioFileData().getId());
        String uri = uriInfo.getBaseUriBuilder().path("/audio/file").path(audioData).build().toString();
        return buildMediaFile(audioEntry.getAudioFileData(), uriInfo, uri);
    }

    public MediaFileDTO buildMediaFile(AudioFileData audioFileData, UriInfo uriInfo, String uri) {
        Validate.notNull(audioFileData, "AudioFileData is null.");
        Validate.notNull(uri, "Uri is null.");

        AudioDTO audio = new AudioDTO(audioFileData.getFilename(), uri, audioFileData.getLastModified(), audioFileData.getSize());
        audio.setAlbumArtist(audioFileData.getMetaData().getAlbumArtist());
        audio.setAlbum(audioFileData.getMetaData().getAlbum());
        audio.setArtist(audioFileData.getMetaData().getArtist());
        audio.setTitle(audioFileData.getMetaData().getTitle());
        audio.setTrackNumber(audioFileData.getMetaData().getTrackNumber());
        audio.setYear(audioFileData.getMetaData().getYear());
        audio.setComposer(audioFileData.getMetaData().getComposer());
        audio.setDuration(audioFileData.getMetaData().getDuration());
        audio.setGenre(audioFileData.getMetaData().getGenre());
        //Set download link
        audio.setDownloadUri(uri + "/download");

        return audio;
    }

    @Override
    public Object buildMediaFileResource(CatalogEntry catalogEntry, UriInfo uriInfo) {
        Validate.notNull(catalogEntry, "CatalogEntry is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        AudioFileData audioFileData = ((AudioCatalogEntry) catalogEntry).getAudioFileData();
        return new AudioFileResource(this, audioFileData, uriInfo);
    }

}
