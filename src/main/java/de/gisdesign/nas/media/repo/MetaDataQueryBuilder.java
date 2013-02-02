package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileType;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Interface use for builders implementation which are capable of building JPA queries
 * used to matched certain media files based on the criteria value.
 * @author Denis Pasek
 */
public interface MetaDataQueryBuilder {

    /**
     * String expression of unknonwn criteria value to be used. Usually maps to
     * a query for null value.
     */
    public static final String UNKNOWN_VALUE = "%unknown%";

    /**
     * Returns the target {@link MediaFileType} of this {@link QueryCriteria}.
     * @return The {@link MediaFileType}.
     */
    public MediaFileType getMediaFileType();

    /**
     * Returns the name of the {@link QueryCriteria}. Used for registering the
     * {@link QueryCriteria} for usage in navigation structure definitions.
     * @return The name of {@link QueryCriteria}.
     */
    public String getName();

    /**
     * Converts the criteria values extracted from the database into the string
     * representations.
     * @param criteriaObjects The list of criteria values as extracted from the database.
     * @return The criteria values in the string representation.
     */
    public List<String> convertCriteriaValues(List<Object> criteriaObjects);

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
    public Predicate buildPredicate(CriteriaBuilder cb, Root<?> root, String criteriaValue);

}
