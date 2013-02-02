package de.gisdesign.nas.media.repo.image;

import java.util.HashMap;
import java.util.Map;

/**
 * Internal configuration model for the {@link ImageMediaRepository}.
 * @author Denis Pasek
 */
class Configuration {

    /**
     * The name of the configuration parameter for the directory holding the resclaed images.
     */
    public static final String CONFIG_PARAM_RESCALED_IMAGE_DIRECTORY = "image.rescale.directory";

    /**
     * The name of the configuration parameter for the size of the slide show images.
     */
    public static final String CONFIG_PARAM_SLIDE_SHOW_SIZE = "image.slide.show.size";

    /**
     * The name of the configuration parameter for the size of the big thumb nails.
     */
    public static final String CONFIG_PARAM_THUMB_BIG_SIZE = "image.thumb.big.size";

    /**
     * The name of the configuration parameter for the size of the small thumb nails.
     */
    public static final String CONFIG_PARAM_THUMB_SMALL_SIZE = "image.thumb.small.size";

    /**
     * The configuration parameter use to configure the image scanning and thumb generation.
     */
    public static final String CONFIG_PARAM_IMAGE_SCAN = "image.scan.cron.expression";

    private static final Map<String,Object> DEFAULTS = new HashMap<String, Object>();
    static {
        DEFAULTS.put(CONFIG_PARAM_IMAGE_SCAN, "0 0 1 * * *");
        DEFAULTS.put(CONFIG_PARAM_SLIDE_SHOW_SIZE, Integer.valueOf(1080));
        DEFAULTS.put(CONFIG_PARAM_THUMB_BIG_SIZE, Integer.valueOf(256));
        DEFAULTS.put(CONFIG_PARAM_THUMB_SMALL_SIZE, Integer.valueOf(128));
        DEFAULTS.put(CONFIG_PARAM_RESCALED_IMAGE_DIRECTORY, System.getProperty("java.io.tmpdir"));
    }

    public static Object getDefault(String paramName)  {
        return DEFAULTS.get(paramName);
    }

    private Configuration()  {
    }


}
