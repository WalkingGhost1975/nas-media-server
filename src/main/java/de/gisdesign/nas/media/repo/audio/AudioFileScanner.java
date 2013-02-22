package de.gisdesign.nas.media.repo.audio;

import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.repo.AbstractMediaFileScanner;
import de.gisdesign.nas.media.repo.MediaFileScanner;
import de.gisdesign.nas.media.repo.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link MediaFileScanner} implementation for {@link AudioFileData} based on the
 * {@link AbstractMediaFileScanner}.
 * @author Denis Pasek
 */
@Component
public class AudioFileScanner extends AbstractMediaFileScanner<AudioFileData> {

    @Autowired
    private AudioMediaRepository mediaRepository;

    @Override
    protected MediaRepository<AudioFileData> getMediaRepository() {
        return mediaRepository;
    }
}
