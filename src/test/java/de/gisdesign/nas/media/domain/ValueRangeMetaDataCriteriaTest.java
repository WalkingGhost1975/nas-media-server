package de.gisdesign.nas.media.domain;

import java.math.BigDecimal;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Denis Pasek
 */
public class ValueRangeMetaDataCriteriaTest {

    private ValueRangeMetaDataCriteria<Integer> integerRange;

    private ValueRangeMetaDataCriteria<BigDecimal> decimalRange;

    @Before
    public void setup() {
        integerRange = new ValueRangeMetaDataCriteria<Integer>("range:integer") {
            @Override
            public Expression<Integer> buildExpression(CriteriaBuilder cb, Root<?> root) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            protected Integer convertFromString(String stringValue) {
                return Integer.valueOf(stringValue);
            }
        };

        decimalRange = new ValueRangeMetaDataCriteria<BigDecimal>("range:decimal") {
            @Override
            public Expression<BigDecimal> buildExpression(CriteriaBuilder cb, Root<?> root) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            protected BigDecimal convertFromString(String stringValue) {
                return new BigDecimal(stringValue);
            }
        };
    }

    @Test(expected=IllegalArgumentException.class)
    public void testShouldThrowIllegalArgumentExceptionInCaseOfInvalidInput() {
        integerRange.setValueAsString("2004-2008");
    }

    @Test
    public void testShouldParseValidIntegerRange() {
        integerRange.setValueAsString("[2004]-[2008]");
        CriteriaRange<Integer> range = integerRange.getRange();
        assertNotNull(range);
        assertEquals(Integer.valueOf(2004), range.getMinValue());
        assertEquals(Integer.valueOf(2008), range.getMaxValue());
    }

    @Test
    public void testShouldParseValidDecimalRange() {
        decimalRange.setValueAsString("[-8.5]-[10.2]");
        CriteriaRange<BigDecimal> range = decimalRange.getRange();
        assertNotNull(range);
        assertEquals(new BigDecimal("-8.5"), range.getMinValue());
        assertEquals(new BigDecimal("10.2"), range.getMaxValue());
    }
}
