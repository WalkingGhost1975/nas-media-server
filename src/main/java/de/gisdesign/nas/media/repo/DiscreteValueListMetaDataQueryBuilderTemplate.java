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
public abstract class DiscreteValueListMetaDataQueryBuilderTemplate<T> implements DiscreteValueListSource<T>, MetaDataQueryBuilder<List<Object>> {

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
    public Predicate buildPredicate(CriteriaBuilder cb, Root<?> root, List<Object> criteriaValue) {
        List<T> convertedValues = new ArrayList<T>(criteriaValue.size());
        for (Object value : criteriaValue) {
            if (value != null)  {
                convertedValues.add(convertStringToValue(String.valueOf(value)));
            }
        }
        Predicate predicate = buildExpression(cb, root).in(convertedValues);
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
