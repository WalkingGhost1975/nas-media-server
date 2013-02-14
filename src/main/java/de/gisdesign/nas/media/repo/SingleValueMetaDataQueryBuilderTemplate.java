package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Denis Pasek
 */
public abstract class SingleValueMetaDataQueryBuilderTemplate implements MetaDataQueryBuilder {

    /**
     * The {@link MediaFileType}.
     */
    private MediaFileType mediaFileType;

    /**
     * The name of the query builder.
     */
    private String name;

    public SingleValueMetaDataQueryBuilderTemplate(MediaFileType mediaFileType, String name) {
        Validate.notNull(mediaFileType, "MediaFileType is null.");
        Validate.notEmpty(name, "Name is empty.");
        this.mediaFileType = mediaFileType;
        this.name = name;
    }

    @Override
    public MediaFileType getMediaFileType() {
        return mediaFileType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> convertCriteriaValues(List<Object> criteriaObjects) {
        List<String> criteriaValues = new ArrayList<String>(criteriaObjects.size());
        for (Object value : criteriaObjects) {
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
    protected abstract String convertValueToString(Object criteriaValue);

    /**
     * Template method to be implemented by subclasses to convert the criteria
     * values string representaion into an object.
     * @param stringValue The string value. Never <code>null</code>.
     * @return The created object.
     */
    protected abstract Object convertStringToValue(String stringValue);

}
