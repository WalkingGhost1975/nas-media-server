package de.gisdesign.nas.media.domain;

import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Denis Pasek
 */
public class ValueListMetaDataCriteria<T> extends MetaDataCriteria<List<T>> {

    private ObjectMapper objectMapper;

    public ValueListMetaDataCriteria(String id) {
        super(id);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String getValueAsString() {
        return (getValue() != null) ? String.valueOf(getValue()) : null;
    }

    public String getValueAsJson() {
        String valueString = null;
        try {
            valueString = objectMapper.writeValueAsString(getValue());
        } catch (IOException ex) {
            throw new RuntimeException("Cannot serialize value list into String.", ex);
        }
        return valueString;
    }

    public void setValueAsJson(String jsonValue)  {
        try {
            List<T> valueList = objectMapper.readValue(jsonValue, List.class);
            setValue(valueList);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot deserialize value list [" + jsonValue + "].", ex);
        }
    }

    @Override
    protected ValueListMetaDataCriteria<T> createClone(String id) {
        return new ValueListMetaDataCriteria<T>(id);
    }

}
