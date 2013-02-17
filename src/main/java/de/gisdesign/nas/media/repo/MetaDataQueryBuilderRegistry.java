package de.gisdesign.nas.media.repo;

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
     * Retrieves a {@link MetaDataQueryBuilder} with the given ID.
     * @param id The unique ID of the {@link MetaDataQueryBuilder}.
     * @return The {@link MetaDataQueryBuilder}.
     */
    public MetaDataQueryBuilder<?> getQueryBuilder(String id)  {
        Validate.notNull(id, "ID is null.");
        if (!applicationContext.containsBean(id))  {
            throw new IllegalStateException("MetaDataQueryBuilder [" + id + "] not available.");
        }
        LOG.debug("Retrieved MetaDataQueryBuilder [{}]", id);
        return applicationContext.getBean(id, MetaDataQueryBuilder.class);
    }

    /**
     * Retrieves a {@link DiscreteValueListSource} with the gievn ID.
     * @param id The unique ID of the {@link DiscreteValueListSource}.
     * @return The {@link DiscreteValueListSource}.
     */
    public DiscreteValueListSource<?> getDiscreteValueListSource(String id)  {
        Validate.notNull(id, "ID is null.");
        if (!applicationContext.containsBean(id))  {
            throw new IllegalStateException("DiscreteValueListSource [" + id + "] not available.");
        }
        LOG.debug("Retrieved DiscreteValueListSource [{}]", id);
        return applicationContext.getBean(id, DiscreteValueListSource.class);
    }

}
