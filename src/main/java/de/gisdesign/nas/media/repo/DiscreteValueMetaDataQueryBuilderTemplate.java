package de.gisdesign.nas.media.repo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Denis Pasek
 */
public abstract class DiscreteValueMetaDataQueryBuilderTemplate<T> implements DiscreteValueListSource<T>, MetaDataQueryBuilder<Object> {

    @Override
    public List<String> convertCriteriaValues(List<T> criteriaObjects) {
        List<String> criteriaValues = new ArrayList<String>(criteriaObjects.size());
        for (T value : criteriaObjects) {
            if (value != null)  {
                criteriaValues.add(convertValueToString(value));
            } else {
                criteriaValues.add(UNKNOWN_VALUE);
            }
        }
        return criteriaValues;
    }

    @Override
    public Predicate buildPredicate(CriteriaBuilder cb, Root<?> root, Object criteriaValue) {
        Predicate predicate;
        if (UNKNOWN_VALUE.equals(criteriaValue) || criteriaValue == null)  {
            predicate = cb.isNull(buildExpression(cb, root));
        } else {
            predicate = cb.equal(buildExpression(cb, root), convertStringToValue(String.valueOf(criteriaValue)));
        }
        return predicate;
    }

    /**
     * Template method to be implemented by subclasses to convert the criteria values to their String representation.
     * @param criteriaValue The criteria value.
     * @return The String representation.
     */
    protected String convertValueToString(T criteriaValue) {
        return String.valueOf(criteriaValue);
    }

    /**
     * Template method to be implemented by subclasses to convert the criteria
     * values string representaion into an object.
     * @param stringValue The string value. Never <code>null</code>.
     * @return The created object.
     */
    protected abstract T convertStringToValue(String stringValue);

}
