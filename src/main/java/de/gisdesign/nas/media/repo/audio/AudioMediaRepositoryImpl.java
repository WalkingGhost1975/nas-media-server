package de.gisdesign.nas.media.repo.audio;

import com.beaglebuddy.mp3.MP3;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.audio.AudioCatalogEntry;
import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.domain.audio.AudioMetaData;
import de.gisdesign.nas.media.domain.audio.Genre;
import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.repo.AbstractMediaRepository;
import de.gisdesign.nas.media.repo.MediaFileDataDAO;
import de.gisdesign.nas.media.repo.MediaFileScanException;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link MediaRepository} implementation used for managing Audio files.
 * @author Denis Pasek
 */
@Component
public class AudioMediaRepositoryImpl extends AbstractMediaRepository<AudioFileData> implements AudioMediaRepository {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AudioMediaRepositoryImpl.class);

    @Autowired
    private AudioRepositoryDAO audioRepositoryDAO;

    public AudioMediaRepositoryImpl() {
        super(MediaFileType.AUDIO);
    }

    @Override
    public CatalogEntry createMediaFileCatalogEntry(CatalogEntry parent, AudioFileData mediaFileData) {
        Validate.notNull(parent, "Parent CatalogEntry is null.");
        Validate.notNull(mediaFileData, "MediaFileData is null.");
        Validate.isTrue(AudioFileData.class.isAssignableFrom(mediaFileData.getClass()), "MediaFileData is not of type AudioFileData.");
        File audioFile = new File(mediaFileData.getAbsolutePath(), mediaFileData.getFilename());
        return audioFile.exists() ? new AudioCatalogEntry(parent, mediaFileData) : null;
    }

    @Transactional
    @Override
    public AudioFileData createMediaFileData(File audioFile) throws MediaFileScanException {
        validateMediaFile(audioFile);
        AudioFileData audioData;
        if (isSupportedMediaFile(audioFile)) {
            audioData = new AudioFileData();
            audioData.setAbsolutePath(audioFile.getParent());
            audioData.setFilename(audioFile.getName());
            scanMediaFileMetaData(audioFile, audioData);
            audioData = audioRepositoryDAO.saveMediaFile(audioData);
        } else {
            throw new MediaFileScanException("Unsupported Audio media file [" + audioFile.getAbsolutePath() + "]");
        }
        return audioData;
    }

    @Override
    public boolean isSupportedMediaFile(File audioFile) {
        validateMediaFile(audioFile);
        //TODO: Extend check for supported audio files.
        return audioFile.getName().toLowerCase().endsWith("mp3");
    }

    @Override
    protected MediaFileDataDAO<AudioFileData> getMediaFileDataDAO() {
        return audioRepositoryDAO;
    }

    @Override
    protected void scanMediaFileMetaData(File mediaFile, AudioFileData mediaFileData) throws MediaFileScanException {
        if (isSupportedMediaFile(mediaFile))  {
            mediaFileData.setLastModified(mediaFile.lastModified());
            mediaFileData.setSize(mediaFile.length());
            try {
                MP3 metadata = new MP3(mediaFile);
                if (metadata.hasErrors())  {
                    LOG.warn("MP3 tag errors for MP3 file [{}]: {}", mediaFile.getAbsolutePath(), metadata.getErrors());
                }
                AudioMetaData audioMetaData = mediaFileData.getMetaData();
                audioMetaData.setAlbum(metadata.getAlbum());
                audioMetaData.setAlbumArtist(metadata.getBand());
                audioMetaData.setArtist(metadata.getBand());
                audioMetaData.setTitle(metadata.getTitle());
                audioMetaData.setComposer(metadata.getMusicBy());
                String musicType = metadata.getMusicType();
                if (musicType != null)  {
                    audioMetaData.setGenre(Genre.getGenreName(musicType));
                }
                audioMetaData.setYear(metadata.getYear());

                audioMetaData.setTrackNumber(metadata.getTrack());
                audioMetaData.setDuration(metadata.getAudioDuration());
            } catch (IOException ex)  {
                throw new MediaFileScanException("IO error during extraction of meta information for Audio file [" + mediaFileData.getAbsolutePath() + "].", ex);
            }
        }
    }
}
