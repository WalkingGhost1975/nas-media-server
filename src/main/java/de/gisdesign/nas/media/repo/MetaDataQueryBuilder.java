package de.gisdesign.nas.media.repo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Interface use for builders implementation which are capable of building JPA queries
 * used to matched certain media files based on the criteria value.
 * @author Denis Pasek
 */
public interface MetaDataQueryBuilder<T> {

    /**
     * Builds an {@link Expression} for the criteria. This expression refers to the entity
     * property which is matched against the criteria.
     * @param cb The {@link CriteriaBuilder}.
     * @param root The entity root {@link Expression}.
     * @return The {@link Expression} pointing to the entity property of the criteria.
     */
    public Expression<?> buildExpression(CriteriaBuilder cb, Root<?> root);

    /**
     * Builds a search {@link Predicate} used to match the criteria value.
     * @param cb The {@link CriteriaBuilder}.
     * @param root The entity root {@link Expression}.
     * @param criteriaValue The criteria value to match.
     * @return The search {@link Predicate}.
     */
    public Predicate buildPredicate(CriteriaBuilder cb, Root<?> root, T criteriaValue);

}
