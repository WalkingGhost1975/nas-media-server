package de.gisdesign.nas.media.repo;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 *
 * @param <T> The type of the discrete values.
 * @author Denis Pasek
 */
public interface DiscreteValueListSource<T> {

    /**
     * String expression of unknown criteria value to be used. Usually maps to
     * a query for null value.
     */
    public static final String UNKNOWN_VALUE = "%unknown%";

    /**
     * Builds an {@link Expression} for the criteria. This expression refers to the entity
     * property which is matched against the criteria.
     * @param cb The {@link CriteriaBuilder}.
     * @param root The entity root {@link Expression}.
     * @return The {@link Expression} pointing to the entity property of the criteria.
     */
    public Expression<T> buildExpression(CriteriaBuilder cb, Root<?> root);

    /**
     * Converts the criteria values extracted from the database into the string
     * representations.
     * @param criteriaObjects The list of criteria values as extracted from the database.
     * @return The criteria values in the string representation.
     */
    public List<String> convertCriteriaValues(List<T> criteriaObjects);

}
