package de.gisdesign.nas.media.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Denis Pasek
 */
public class ValueListMetaDataCriteriaTest {

    private ValueListMetaDataCriteria<String> stringListCriteria;

    private ValueListMetaDataCriteria<Integer> integerListCriteria;

    @Before
    public void setup() {
        stringListCriteria = new ValueListMetaDataCriteria<String>("string:list") {
            @Override
            public Expression<String> buildExpression(CriteriaBuilder cb, Root<?> root) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        integerListCriteria = new ValueListMetaDataCriteria<Integer>("integer:list") {
            @Override
            public Expression<Integer> buildExpression(CriteriaBuilder cb, Root<?> root) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Test
    public void testShouldRenderNullList() {
        stringListCriteria.setValueList(null);
        assertEquals("[]", stringListCriteria.getValueAsString());
    }

    @Test
    public void testShouldRenderEmptyList() {
        stringListCriteria.setValueList(Collections.<String>emptyList());
        assertEquals("[]", stringListCriteria.getValueAsString());
    }

    @Test
    public void testShouldParseValidStringList() {
        stringListCriteria.setValueAsString("[\"Electronica\",\"Psychedelic\"]");
        List<String> valueList = stringListCriteria.getValueList();
        assertEquals(2, valueList.size());
        assertTrue(valueList.contains("Electronica"));
        assertTrue(valueList.contains("Psychedelic"));
    }

    @Test
    public void testShouldRenderStringList() {
        stringListCriteria.setValueList(Arrays.asList("Electronica","Psychedelic"));
        assertEquals("[\"Electronica\",\"Psychedelic\"]", stringListCriteria.getValueAsString());
    }


    @Test
    public void testShouldParseValidIntegerList() {
        integerListCriteria.setValueAsString("[2004,2005]");
        List<Integer> valueList = integerListCriteria.getValueList();
        assertEquals(2, valueList.size());
        assertTrue(valueList.contains(Integer.valueOf(2004)));
        assertTrue(valueList.contains(Integer.valueOf(2005)));
    }

    @Test
    public void testShouldRenderIntegerList() {
        integerListCriteria.setValueList(Arrays.asList(Integer.valueOf(2004),Integer.valueOf(2005)));
        assertEquals("[2004,2005]", integerListCriteria.getValueAsString());
    }
}
