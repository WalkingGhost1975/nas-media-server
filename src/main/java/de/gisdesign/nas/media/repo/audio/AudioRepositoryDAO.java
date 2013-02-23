package de.gisdesign.nas.media.repo.audio;

import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.audio.AudioFileData;
import java.util.List;

/**
 *
 * @author Denis Pasek
 */
interface AudioRepositoryDAO {

    public AudioFileData findAudioFileById(Long id);

    public AudioFileData findAudioFileByAbsoluteFileName(String absoluteFileName);

    public List<AudioFileData> findAudioFilesByDirectory(String directoryName);

    public List<AudioFileData> findAudioFilesByCriteria(MetaDataCriteria<?> criteria);

    public long countAudioFilesMatchingCriteria(MetaDataCriteria<?> metaDataCriteria);

    public <T> List<T> loadAudioFileCriteriaValues(MetaDataCriteria<T> criteria);

    public AudioFileData saveAudioFile(AudioFileData imageFileData);

    public void deleteAudioFile(AudioFileData mediaFileData);

}
