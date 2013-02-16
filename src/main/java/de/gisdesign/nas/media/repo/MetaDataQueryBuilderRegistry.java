package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileType;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Registry for implementations of the {@link MetaDataQueryBuilder} interface. Instances
 * of this type should be automatically discovered in the Spring context and are added
 * to this registry.
 * @author Denis Pasek
 */
@Component
public class MetaDataQueryBuilderRegistry {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MetaDataQueryBuilderRegistry.class);

    /**
     * The autowired list of all {@link MetaDataQueryBuilder}s.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Retrieves a {@link MetaDataQueryBuilder} for the specified {@link MediaFileType} with the given name.
     * @param <T> The concrete target type of the {@link MetaDataQueryBuilder}.
     * @param mediaFileType The {@link MediaFileType}.
     * @param name The name of the {@link MetaDataQueryBuilder}.
     * @param type The target type of the {@link MetaDataQueryBuilder}.
     * @return The {@link MetaDataQueryBuilder} or <code>null</code> is not present.
     */
    public <T extends MetaDataQueryBuilder> T getQueryBuilder(MediaFileType mediaFileType, String name, Class<T> type)  {
        Validate.notNull(mediaFileType, "MediaFileType is null.");
        Validate.notNull(name, "Name is null.");
        Validate.notNull(type, "Type is null.");
        Map<String,T> queryBuilders = applicationContext.getBeansOfType(type);
        T queryBuilder = null;
        for (T builder : queryBuilders.values()) {
            if (builder.getMediaFileType() == mediaFileType && builder.getName().equals(name))  {
                LOG.debug("Retrieved MetaDataQueryBuilder [{}] for MediaFileType [{}]", name, mediaFileType);
                queryBuilder = builder;
                break;
            }
        }
        if (queryBuilder == null) {
            LOG.error("Retrieved MetaDataQueryBuilder [{}] for MediaFileType [{}] unavailable.", name, mediaFileType);
            throw new IllegalStateException("MetaDataQueryBuilder with name [" + name + "] for MediaFileType [" + mediaFileType + "] unavailable.");
        }
        return queryBuilder;
    }
}
