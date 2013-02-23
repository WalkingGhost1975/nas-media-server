package de.gisdesign.nas.media.repo.audio;

import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.repo.AbstractMediaFileDAO;
import de.gisdesign.nas.media.repo.MediaFileDataDAO;
import org.springframework.stereotype.Repository;

/**
 * {@link MediaFileDataDAO} implementation for {@link AudioFileData}. Is based on
 * the {@link AbstractMediaFileDAO}.
 * @author Denis Pasek
 */
@Repository
public class AudioRepositoryDAOImpl extends AbstractMediaFileDAO<AudioFileData> implements AudioRepositoryDAO {

    public AudioRepositoryDAOImpl() {
        super(AudioFileData.class);
    }
}
