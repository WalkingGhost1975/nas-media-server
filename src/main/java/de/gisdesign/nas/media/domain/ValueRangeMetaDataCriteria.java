package de.gisdesign.nas.media.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @param <V> The type of the value range.
 * @author Denis Pasek
 */
public abstract class ValueRangeMetaDataCriteria<V extends Comparable<V>> extends MetaDataCriteria<V> {

    /**
     * The parsing regex pattern.
     */
    private static final Pattern PATTERN = Pattern.compile("\\[((\\-?)\\d+(\\.\\d+)?)\\]\\-\\[((\\-?)\\d+(\\.\\d+)?)\\]");

    /**
     * The criteria range.
     */
    private CriteriaRange<V> range;

    /**
     * Constructor.
     * @param id The ID of the criteria.
     */
    public ValueRangeMetaDataCriteria(String id) {
        super(id);
    }

    public CriteriaRange<V> getRange() {
        return range;
    }

    public void setRange(CriteriaRange<V> range) {
        this.range = range;
    }

    @Override
    public Predicate buildPredicate(CriteriaBuilder cb, Root<?> root) {
        Predicate predicate = null;
        Expression<V> expression = buildExpression(cb, root);
        if (range.getMinValue() != null)  {
            predicate = cb.greaterThanOrEqualTo(expression, range.getMinValue());
        }
        if (range.getMaxValue() != null)  {
            Predicate maxValue = cb.lessThanOrEqualTo(expression, range.getMaxValue());
            predicate = (predicate != null) ? cb.and(predicate, maxValue) : maxValue;
        }
        return predicate;
    }

    @Override
    public abstract Expression<V> buildExpression(CriteriaBuilder cb, Root<?> root);

    @Override
    public String getValueAsString() {
        StringBuilder valueString = new StringBuilder();
        valueString.append('[').append(range.getMinValue()).append(']');
        valueString.append('-');
        valueString.append('[').append(range.getMaxValue()).append(']');
        return valueString.toString();
    }

    @Override
    public void setValueAsString(String valueString)  {
        range = null;
        if (valueString != null) {
            Matcher matcher = PATTERN.matcher(valueString);
            if (matcher.matches()) {
                V minValue = convertFromString(matcher.group(1));
                V maxValue = convertFromString(matcher.group(4));
                range = new CriteriaRange<V>(minValue, maxValue);
            } else {
                throw new IllegalArgumentException("Invalid range string [" + valueString + "].");
            }
        }
    }

    @Override
    public String convertToString(V value) {
        return String.valueOf(value);
    }

    protected abstract V convertFromString(String stringValue);
}
