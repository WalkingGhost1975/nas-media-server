package de.gisdesign.nas.media.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Factory for {@link MetaDataCriteriaFactory}.
 * @author Denis Pasek
 */
@Component
public class MetaDataCriteriaFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public <V> MetaDataCriteria<V> createMetaDataCriteria(String id)  {
        return applicationContext.getBean(id, MetaDataCriteria.class);
    }

}
