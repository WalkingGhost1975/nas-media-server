package de.gisdesign.nas.media.repo.audio;

import com.beaglebuddy.mp3.MP3;
import de.gisdesign.nas.media.admin.ConfigurationService;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.DiscreteValueMetaDataCriteria;
import de.gisdesign.nas.media.domain.audio.AudioCatalogEntry;
import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.domain.audio.AudioMetaData;
import de.gisdesign.nas.media.domain.audio.Genre;
import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.repo.MediaFileLibraryManager;
import de.gisdesign.nas.media.repo.MediaFileScanException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Denis Pasek
 */
@Component
public class AudioMediaRepositoryImpl implements AudioMediaRepository {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AudioMediaRepositoryImpl.class);

    @Autowired
    private ConfigurationService configService;

    @Autowired
    private MediaFileLibraryManager mediaFileLibraryManager;

    @Autowired
    private AudioRepositoryDAO audioRepositoryDAO;

    @Override
    public List<String> getMediaFileLibraryNames() {
        return this.mediaFileLibraryManager.getMediaFileLibraryNames(MediaFileType.AUDIO);
    }

    @Override
    public MediaFileLibrary getMediaFileLibrary(String libraryName) {
        return this.mediaFileLibraryManager.getMediaFileLibrary(MediaFileType.AUDIO, libraryName);
    }

    @Override
    public MediaFileType getSupportedMediaFileType() {
        return MediaFileType.AUDIO;
    }

    @Override
    public CatalogEntry createMediaFileCatalogEntry(CatalogEntry parent, AudioFileData mediaFileData) {
        Validate.notNull(parent, "Parent CatalogEntry is null.");
        Validate.notNull(mediaFileData, "MediaFileData is null.");
        Validate.isTrue(AudioFileData.class.isAssignableFrom(mediaFileData.getClass()), "MediaFileData is not of type AudioFileData.");
        File audioFile = new File(mediaFileData.getAbsolutePath(), mediaFileData.getFilename());
        return audioFile.exists() ? new AudioCatalogEntry(parent, mediaFileData) : null;
    }

    @Override
    public AudioFileData loadMediaFileData(Long id) {
        Validate.notNull(id, "ID is null.");
        return audioRepositoryDAO.findAudioFileById(id);
    }

    @Override
    public AudioFileData loadMediaFileData(File audioFile) {
        validateMediaFile(audioFile);
        return audioRepositoryDAO.findAudioFileByAbsoluteFileName(audioFile.getAbsolutePath());
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
            audioData.setLastModified(audioFile.lastModified());
            audioData.setSize(audioFile.length());
            scanAudioFileMetaData(audioFile, audioData);
            audioData = audioRepositoryDAO.saveAudioFile(audioData);
        } else {
            throw new MediaFileScanException("Unsupported Audio media file [" + audioFile.getAbsolutePath() + "]");
        }
        return audioData;
    }

    @Transactional
    @Override
    public AudioFileData updateMediaFileData(AudioFileData mediaFileData) throws MediaFileScanException {
        File audioFile = new File(mediaFileData.getAbsolutePath(), mediaFileData.getFilename());
        //Only rescan meta data if timestamp has changed.
        if (mediaFileData.hasChanged(audioFile.lastModified())) {
            scanAudioFileMetaData(audioFile, mediaFileData);
            mediaFileData.setLastModified(audioFile.lastModified());
            mediaFileData.setSize(audioFile.length());
        }
        //Update sync ID and store metadata.
        AudioFileData updatedAudioFileData = audioRepositoryDAO.saveAudioFile(mediaFileData);
        return updatedAudioFileData;
    }

    @Transactional
    @Override
    public void deleteMediaFileData(AudioFileData mediaFileData) {
        audioRepositoryDAO.deleteAudioFile(mediaFileData);
    }

    @Override
    public Map<String,AudioFileData> loadMediaFilesFromDirectory(File directory) {
        validateMediaFileDirectory(directory);
        List<AudioFileData> mediaFileDataList = audioRepositoryDAO.findAudioFilesByDirectory(directory.getAbsolutePath());
        Map<String,AudioFileData> mediaFileDataMap = new HashMap<String, AudioFileData>(mediaFileDataList.size()*2);
        LOG.debug("Loaded {} MediaFileData for media files in directory [{}].", mediaFileDataList.size(), directory.getAbsolutePath());
        for (AudioFileData mediaFileData : mediaFileDataList) {
            mediaFileDataMap.put(mediaFileData.getFilename(), mediaFileData);
        }
        return mediaFileDataMap;
    }

    @Override
    public boolean isSupportedMediaFile(File audioFile) {
        validateMediaFile(audioFile);
        //TODO: Extend check for supported audio files.
        return audioFile.getName().toLowerCase().endsWith("mp3");
    }

    @Override
    public List<AudioFileData> findMediaFilesByCriteria(MetaDataCriteria<?> criteria) {
        List<AudioFileData> audioFiles = audioRepositoryDAO.findAudioFilesByCriteria(criteria);
        LOG.debug("Loaded [{}] Audio files for MetaDataCriteria [{}]", audioFiles.size(), criteria.dumpHierarchy());
        return audioFiles;
    }

    @Override
    public List<DiscreteValueMetaDataCriteria> loadMetaDataCriteriaOptions(MetaDataCriteria<?> criteria) {
        List<String> criteriaValues = audioRepositoryDAO.loadAudioFileCriteriaValues(criteria);
        LOG.debug("Loaded MetaDataCriteriaValues {} for MetaDataCriteria [{}]", criteriaValues, criteria.dumpHierarchy());
        List<DiscreteValueMetaDataCriteria> criteriaChildren = new ArrayList<DiscreteValueMetaDataCriteria>(criteriaValues.size());
        for (String value : criteriaValues) {
            DiscreteValueMetaDataCriteria newMetaDataCriteria = new DiscreteValueMetaDataCriteria(criteria.getId());
            newMetaDataCriteria.setValue(value);
            if (criteria.getParent() != null)  {
                newMetaDataCriteria.setParent(criteria.getParent());
            }
            if (criteria.getChildCriteria() != null)  {
                newMetaDataCriteria.setChildCriteria(criteria.getChildCriteria().copy());
            }
            criteriaChildren.add(newMetaDataCriteria);
        }
        return criteriaChildren;
    }

    private void validateMediaFileDirectory(File directory) {
        Validate.notNull(directory, "Directory is null.");
        Validate.isTrue(directory.exists(), "Directory [" + directory.getAbsolutePath() + "] does not exist.");
        Validate.isTrue(directory.isDirectory(), "File [" + directory.getAbsolutePath() + "] is not a directory.");
    }

    private void validateMediaFile(File audioFile) {
        Validate.notNull(audioFile, "AudioFile is null.");
        Validate.isTrue(audioFile.exists(), "AudioFile [" + audioFile.getAbsolutePath() + "] does not exist.");
        Validate.isTrue(audioFile.isFile(), "AudioFile [" + audioFile.getAbsolutePath() + "] is not a file.");
    }

    private void scanAudioFileMetaData(File audioFile, AudioFileData audioData) throws MediaFileScanException {
        if (isSupportedMediaFile(audioFile))  {
            try {
                MP3 metadata = new MP3(audioFile);
                if (metadata.hasErrors())  {
                    LOG.warn("MP3 tag errors for MP3 file [{}]: {}", audioFile.getAbsolutePath(), metadata.getErrors());
                }
                AudioMetaData audioMetaData = audioData.getMetaData();
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
                throw new MediaFileScanException("IO error during extraction of meta information for Audio file [" + audioFile.getAbsolutePath() + "].", ex);
            }
        }
    }

}
