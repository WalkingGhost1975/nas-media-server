package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Collection<MetaDataQueryBuilder> queryBuilders;

    /**
     * The registration map of the {@link MetaDataQueryBuilder}s.
     */
    private Map<String,MetaDataQueryBuilder> queryBuilderMap = new HashMap<String, MetaDataQueryBuilder>();

    @PostConstruct
    public void initialize()  {
        //Copy MetaDataQueryBuilder to map for registration.
        for (MetaDataQueryBuilder queryBuilder : queryBuilders) {
            Validate.notNull(queryBuilder.getMediaFileType(), "MediaFileType of MetaDataQueryBuilder of type [" + queryBuilder.getClass().getName() + "] is null.");
            Validate.notEmpty(queryBuilder.getName(), "Name of MetaDataQueryBuilder of type [" + queryBuilder.getClass().getName() + "] is empty.");
            queryBuilderMap.put(buildIdentifier(queryBuilder.getMediaFileType(), queryBuilder.getName()), queryBuilder);
        }
        LOG.info("Registered {} MetaDataQueryBuilders: {}", queryBuilderMap.size(), queryBuilderMap.keySet());
    }

    /**
     * Retrieves a {@link MetaDataQueryBuilder} for the specified {@link MediaFileType} with the given name.
     * @param mediaFileType The {@link MediaFileType}.
     * @param name The name of the {@link MetaDataQueryBuilder}.
     * @return The {@link MetaDataQueryBuilder} or <code>null</code> is not present.
     */
    public MetaDataQueryBuilder getQueryBuilder(MediaFileType mediaFileType, String name)  {
        Validate.notNull(mediaFileType, "MediaFileType is null.");
        Validate.notNull(name, "Name is null.");
        return queryBuilderMap.get(buildIdentifier(mediaFileType, name));
    }

    private String buildIdentifier(MediaFileType mediaFileType, String name)  {
        String id = mediaFileType.getNamespace() + ":" + name;
        return id;
    }
}
