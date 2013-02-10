package de.gisdesign.nas.media.repo.audio;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.AbstractScanCronTrigger;
import static de.gisdesign.nas.media.repo.audio.Configuration.*;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;

/**
 * {@link Trigger} implementation used to schedule the behavior of audio scanning.
 * @author Denis Pasek
 */
@Component
public class AudioScanCronTrigger extends AbstractScanCronTrigger {
    /**
     * Constructor.
     */
    public AudioScanCronTrigger() {
        super(MediaFileType.AUDIO, CONFIG_PARAM_AUDIO_SCAN, String.valueOf(getDefault(CONFIG_PARAM_AUDIO_SCAN)));
    }
}
