package de.gisdesign.nas.media.domain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.Validate;

/**
 * Represents a node in a hierarchy of criteria used to match certain media files.
 * @param <V> The type of value supported.
 * @author Denis Pasek
 */
public abstract class MetaDataCriteria<V> {

    /**
     * String expression of unknown criteria value to be used. Usually maps to
     * a query for null value.
     */
    public static final String UNKNOWN_VALUE = "%unknown%";

    /**
     * The ID of the criteria.
     */
    private String id;

    /**
     * The parent criteria.
     */
    private MetaDataCriteria<?> parent;

    /**
     * The child criteria.
     */
    private MetaDataCriteria<?> childCriteria;

    /**
     * Constructor.
     * @param id The ID of the criteria.
     */
    public MetaDataCriteria(String id) {
        Validate.notNull(id, "ID is null.");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public MetaDataCriteria<?> getParent() {
        return parent;
    }

    public void setParent(MetaDataCriteria<?> parent) {
        this.parent = parent;
    }

    public MetaDataCriteria<?> getChildCriteria() {
        return childCriteria;
    }

    public void setChildCriteria(MetaDataCriteria<?> childCriteria) {
        this.childCriteria = childCriteria;
        this.childCriteria.setParent(this);
    }

    public abstract String getValueAsString();

    public abstract void setValueAsString(String stringValue);

    public abstract String convertToString(V value);

    /**
     * Builds an {@link Expression} for the criteria. This expression refers to the entity
     * property which is matched against the criteria.
     * @param cb The {@link CriteriaBuilder}.
     * @param root The entity root {@link Expression}.
     * @return The {@link Expression} pointing to the entity property of the criteria.
     */
    public abstract Expression<V> buildExpression(CriteriaBuilder cb, Root<?> root);

    /**
     * Builds a search {@link Predicate} used to match the criteria value.
     * @param cb The {@link CriteriaBuilder}.
     * @param root The entity root {@link Expression}.
     * @return The search {@link Predicate}.
     */
    public abstract Predicate buildPredicate(CriteriaBuilder cb, Root<?> root);

    /**
     * Dumps the hoerachy of the {@link MetaDataCriteria} into a human readble form.
     * Only used for debugging purposes.
     * @return The string representation of the {@link MetaDataCriteria} hierachy.
     */
    public String dumpHierarchy()  {
        MetaDataCriteria<?> currentCriteria = this;
        StringBuilder sb = new StringBuilder();
        int level = 0;
        while (currentCriteria != null) {
            StringBuilder criteria = new StringBuilder();
            criteria.append(currentCriteria.getId());
            if (level > 0) {
                criteria.append("/");
            }
            sb.insert(0, criteria);
            currentCriteria = currentCriteria.getParent();
            level++;
        }
        return sb.toString();
    }
}
