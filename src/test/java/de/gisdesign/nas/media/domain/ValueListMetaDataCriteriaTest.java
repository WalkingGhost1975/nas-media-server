package de.gisdesign.nas.media.domain;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Denis Pasek
 */
public class ValueListMetaDataCriteriaTest {

    private ValueListMetaDataCriteria criteria;


    public ValueListMetaDataCriteriaTest() {
    }

    @Before
    public void setup()  {
        criteria = new ValueListMetaDataCriteria("audio:criteria");
    }

    @Test
    public void testShouldSerializeListOfStringValuesIntoJson() {
        List<String> valueList = new ArrayList<String>(5);
        valueList.add("Electronica");
        valueList.add("Space Rock");
        criteria.setValue(valueList);
        String jsonString = criteria.getValueAsJson();
        assertNotNull(jsonString);
        assertEquals("[\"Electronica\",\"Space Rock\"]", jsonString);
    }

    @Test
    public void testShouldDeserializeListOfStringValuesIntoJson() {
        String jsonString = "[\"Electronica\",\"Space Rock\"]";
        criteria.setValueAsJson(jsonString);
        assertNotNull(criteria.getValue());
        List<String> valueList = new ArrayList<String>(5);
        valueList.add("Electronica");
        valueList.add("Space Rock");
        assertEquals(valueList, criteria.getValue());
    }

}
