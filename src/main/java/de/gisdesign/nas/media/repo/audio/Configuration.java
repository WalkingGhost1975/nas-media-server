package de.gisdesign.nas.media.repo.audio;

import java.util.HashMap;
import java.util.Map;

/**
 * Internal configuration model for the {@link ImageMediaRepository}.
 * @author Denis Pasek
 */
final class Configuration {

    /**
     * The configuration parameter use to configure the audio scanning.
     */
    public static final String CONFIG_PARAM_AUDIO_SCAN = "audio.scan.cron.expression";

    private static final Map<String,Object> DEFAULTS = new HashMap<String, Object>();
    static {
        DEFAULTS.put(CONFIG_PARAM_AUDIO_SCAN, "0 0 2 * * *");
    }

    public static Object getDefault(String paramName)  {
        return DEFAULTS.get(paramName);
    }

    private Configuration()  {
    }


}
