package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.admin.ConfigurationService;
import de.gisdesign.nas.media.domain.MediaFileType;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

/**
 * Base class for scanning cron triggers.
 * @author Denis Pasek
 */
public abstract class AbstractScanCronTrigger implements Trigger {

    /**
     * The supported media file type.
     */
    private MediaFileType mediaFileType;

    /**
     * The default Cron expression
     */
    private String defaultCronExpression;

    /**
     * The nae of the configuration parameter for the Cron expression.
     */
    private String configurationParameterName;

    @Autowired
    private ConfigurationService configurationService;

    /**
     * Constructor.
     * @param mediaFileType The supported {@link MediaFileType}.
     * @param configurationParameterName The configuration parameter name.
     * @param defaultCronExpression The default Cron expression to use.
     */
    public AbstractScanCronTrigger(MediaFileType mediaFileType, String configurationParameterName, String defaultCronExpression) {
        this.mediaFileType = mediaFileType;
        this.configurationParameterName = configurationParameterName;
        this.defaultCronExpression = defaultCronExpression;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        String scanCronExpression = configurationService.getConfigurationParameter(mediaFileType, configurationParameterName);
        if (scanCronExpression == null)  {
            scanCronExpression = defaultCronExpression;
            configurationService.setConfigurationParameter(mediaFileType, configurationParameterName, scanCronExpression);
        }
        CronTrigger cronTrigger = new CronTrigger(scanCronExpression);
        return cronTrigger.nextExecutionTime(triggerContext);
    }
}
