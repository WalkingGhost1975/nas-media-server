package de.gisdesign.nas.media.domain;

import java.io.IOException;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Denis Pasek
 */
public abstract class ValueListMetaDataCriteria<V> extends MetaDataCriteria<V> {

    private List<V> valueList;

    private ObjectMapper objectMapper;

    public ValueListMetaDataCriteria(String id) {
        super(id);
        this.objectMapper = new ObjectMapper();
    }

    public String getValueAsJson() {
        String valueString = null;
        try {
            valueString = objectMapper.writeValueAsString(valueList);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot serialize value list into String.", ex);
        }
        return valueString;
    }

    public void setValueAsJson(String jsonValue)  {
        try {
            this.valueList = objectMapper.readValue(jsonValue, List.class);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot deserialize value list [" + jsonValue + "].", ex);
        }
    }

    @Override
    public Predicate buildPredicate(CriteriaBuilder cb, Root<?> root) {
        return buildExpression(cb, root).in(valueList);
    }

    @Override
    public String convertToString(V value) {
        return String.valueOf(value);
    }
}
