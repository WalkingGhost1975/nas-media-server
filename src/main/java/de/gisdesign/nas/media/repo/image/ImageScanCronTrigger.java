package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.AbstractScanCronTrigger;
import static de.gisdesign.nas.media.repo.image.Configuration.*;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;

/**
 * {@link Trigger} implementation used to schedule the behavior of image scanning and thumb generation.
 * @author Denis Pasek
 */
@Component
public class ImageScanCronTrigger extends AbstractScanCronTrigger {
    /**
     * Constructor.
     */
    public ImageScanCronTrigger() {
        super(MediaFileType.IMAGE, CONFIG_PARAM_IMAGE_SCAN, String.valueOf(getDefault(CONFIG_PARAM_IMAGE_SCAN)));
    }
}
