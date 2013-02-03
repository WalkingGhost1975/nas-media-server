package de.gisdesign.nas.media.admin;

import de.gisdesign.nas.media.domain.MediaFileType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of the {@link ConfigurationService} interface. Directly interacts
 * with JPA persistence to manage configuration parameters.
 * @author Denis Pasek
 */
@Service
public class ConfigurationServiceImpl implements ConfigurationService {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public String getConfigurationParameter(MediaFileType mediaFileType, String parameterName) {
        ConfigurationParameter configParameter = loadConfigurationParameter(mediaFileType, parameterName);
        String configValue = (configParameter != null) ? configParameter.getValue() : null;
        LOG.debug("Loaded configuration parameter [{}:{}] with value [{}].", mediaFileType, parameterName, configValue);
        return configValue;
    }

    @Transactional
    @Override
    public void setConfigurationParameter(MediaFileType mediaFileType, String parameterName, String parameterValue) {
        ConfigurationParameter configParameter = loadConfigurationParameter(mediaFileType, parameterName);
        if (configParameter != null)  {
            configParameter.updateValue(parameterValue);
        } else {
            configParameter = new ConfigurationParameter(mediaFileType, parameterName, parameterValue);
            em.persist(configParameter);
        }
        LOG.debug("Updated configuration parameter [{}:{}] to value [{}].", mediaFileType, parameterName, parameterValue);
    }

    /**
     * Loads the specified configuration parameter for a {@link MediaFileType}.
     * @param mediaFileType The {@link MediaFileType}.
     * @param parameterName The name of the configuratiom parameter.
     * @return The value of the configuration parameter or <code>null</code> if not present.
     */
    private ConfigurationParameter loadConfigurationParameter(MediaFileType mediaFileType, String parameterName) {
        TypedQuery<ConfigurationParameter> query = em.createQuery("SELECT cp FROM ConfigurationParameter cp WHERE cp.mediaFileType=?1 AND cp.name=?2", ConfigurationParameter.class);
        query.setParameter(1, mediaFileType);
        query.setParameter(2, parameterName);
        ConfigurationParameter configParameter = null;
        try {
            configParameter = query.getSingleResult();
        } catch (NoResultException ex)  {
            LOG.debug("Configuration parameter [{}] for media file type [{}] not found.", parameterName, mediaFileType);
        }
        return configParameter;
    }
}
