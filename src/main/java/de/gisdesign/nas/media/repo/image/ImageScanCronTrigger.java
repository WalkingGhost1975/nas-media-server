package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.admin.ConfigurationService;
import de.gisdesign.nas.media.domain.MediaFileType;
import static de.gisdesign.nas.media.repo.image.Configuration.*;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * {@link Trigger} implementation used to schedule the behavior of image scanning and thumb generation.
 * @author Denis Pasek
 */
@Component
public class ImageScanCronTrigger implements Trigger {

    @Autowired
    private ConfigurationService administrationService;

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        String imageScanCronExpression = administrationService.getConfigurationParameter(MediaFileType.IMAGE, CONFIG_PARAM_IMAGE_SCAN);
        if (imageScanCronExpression == null)  {
            imageScanCronExpression = (String) getDefault(CONFIG_PARAM_IMAGE_SCAN);
            administrationService.setConfigurationParameter(MediaFileType.IMAGE, CONFIG_PARAM_IMAGE_SCAN, imageScanCronExpression);
        }
        CronTrigger cronTrigger = new CronTrigger(imageScanCronExpression);
        return cronTrigger.nextExecutionTime(triggerContext);
    }
}
