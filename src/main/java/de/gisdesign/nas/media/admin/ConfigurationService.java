package de.gisdesign.nas.media.admin;

import de.gisdesign.nas.media.domain.MediaFileType;

/**
 * Service interface for the Media Repository. Allows management of the
 * configuration parameters and the management of the media libraries.
 * @author Denis Pasek
 */
public interface ConfigurationService {

    /**
     * Generic method used to load a {@link MediaFileType} specific configuration parameter.
     * @param mediaFileType The {@link MediaFileType}. May not be <code>null</code>.
     * @param parameterName The name of the parameter to retrieve.
     * @return The configuration value or <code>null</code> if not yet set.
     */
    public String getConfigurationParameter(MediaFileType mediaFileType, String parameterName);

    /**
     * Generic method used to set a {@link MediaFileType} specific configuration parameter.
     * @param mediaFileType The {@link MediaFileType}. May not be <code>null</code>.
     * @param parameterName The name of the parameter to set.
     * @param parameterValue The parameter value to set. Can be any kind of string representation, even a serialized/marshalled form
     * of data up to 4k.
     */
    public void setConfigurationParameter(MediaFileType mediaFileType, String parameterName, String parameterValue);

}
