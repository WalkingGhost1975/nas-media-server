package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.CriteriaRange;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.Validate;

/**
 *
 * @param <T> The supported range type.
 * @author Denis Pasek
 */
public abstract class ValueRangeMetaDataQueryBuilderTemplate<T extends Comparable<T>> implements MetaDataQueryBuilder<CriteriaRange<?>> {

    @Override
    public abstract Expression<T> buildExpression(CriteriaBuilder cb, Root<?> root);

    @Override
    public Predicate buildPredicate(CriteriaBuilder cb, Root<?> root, CriteriaRange<?> criteriaValue) {
        Validate.isTrue(criteriaValue instanceof CriteriaRange, "Criteria value not of type CriteriaRange.");
        Predicate predicate = null;
        if (criteriaValue.getMinValue() != null)  {
            predicate = cb.greaterThanOrEqualTo(buildExpression(cb, root), convertStringToValue(String.valueOf(criteriaValue.getMinValue())));
        }
        if (criteriaValue.getMaxValue() != null)  {
            Predicate maxValue = cb.lessThanOrEqualTo(buildExpression(cb, root), convertStringToValue(String.valueOf(criteriaValue.getMaxValue())));
            predicate = (predicate != null) ? cb.and(predicate, maxValue) : maxValue;
        }
        return predicate;
    }

    /**
     * Template method to be implemented by subclasses to convert the criteria
     * values string representaion into an object.
     * @param stringValue The string value. Never <code>null</code>.
     * @return The created object.
     */
    protected abstract T convertStringToValue(String stringValue);
}
