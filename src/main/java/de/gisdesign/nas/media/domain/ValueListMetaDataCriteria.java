package de.gisdesign.nas.media.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * The list of values.
     */
    private List<V> valueList = Collections.emptyList();

    /**
     * The {@link ObjectMapper} used for serialization.
     */
    private ObjectMapper objectMapper;

    /**
     * Constructor.
     * @param id The ID of the criteria.
     */
    public ValueListMetaDataCriteria(String id) {
        super(id);
        this.objectMapper = new ObjectMapper();
    }

    public List<V> getValueList() {
        return new ArrayList<V>(valueList);
    }

    public void setValueList(List<V> valueList) {
        this.valueList = (valueList != null) ? new ArrayList<V>(valueList) : Collections.<V>emptyList();
    }

    @Override
    public String getValueAsString() {
        String valueString = null;
        try {
            valueString = objectMapper.writeValueAsString(valueList);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot serialize value list into String.", ex);
        }
        return valueString;
    }

    @Override
    public void setValueAsString(String jsonValue)  {
        try {
            this.valueList = objectMapper.readValue(jsonValue, List.class);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot deserialize value list [" + jsonValue + "].", ex);
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
