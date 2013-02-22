package de.gisdesign.nas.media.domain;

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

    private CriteriaRange<V> range;

    public CriteriaRange<V> getRange() {
        return range;
    }

    public void setRange(CriteriaRange<V> range) {
        this.range = range;
    }

    public ValueRangeMetaDataCriteria(String id) {
        super(id);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValueAsString(String stringValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
