package de.gisdesign.nas.media.domain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Denis Pasek
 */
public abstract class DiscreteValueMetaDataCriteria<V> extends MetaDataCriteria<V> {

    /**
     * The criteria value.
     */
    private V value;

    public DiscreteValueMetaDataCriteria(String id) {
        super(id);
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Checks whether this criteria is a leaf criteria. Leaf criteria can be used
     * to match a set of media files while non-leaf criteria can used to match
     * a list of possible criteria options.
     * @return <code>true</code> if this criteria is leaf criteria.
     */
    public boolean isLeaf() {
        return (value != null && getChildCriteria() == null);
    }


    @Override
    public final String getValueAsString()  {
        return convertToString(value);
    }

    @Override
    public String convertToString(V value)   {
        return (value != null) ? String.valueOf(value) : UNKNOWN_VALUE;
    }

    @Override
    public void setValueAsString(String stringValue)  {
        if (stringValue == null || UNKNOWN_VALUE.equals(stringValue))  {
            setValue(null);
        } else {
            setValue(convertStringToValue(stringValue));
        }
    }

    @Override
    public Predicate buildPredicate(CriteriaBuilder cb, Root<?> root) {
        Predicate predicate;
        if (getValue() == null)  {
            predicate = cb.isNull(buildExpression(cb, root));
        } else {
            predicate = cb.equal(buildExpression(cb, root), getValue());
        }
        return predicate;
    }

    protected abstract V convertStringToValue(String stringValue);
}
