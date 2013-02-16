package de.gisdesign.nas.media.repo;

import java.util.List;

/**
 *
 * @author Denis Pasek
 */
public interface CatalogMetaDataQueryBuilder extends MetaDataQueryBuilder {

    /**
     * String expression of unknown criteria value to be used. Usually maps to
     * a query for null value.
     */
    public static final String UNKNOWN_VALUE = "%unknown%";

    /**
     * Converts the criteria values extracted from the database into the string
     * representations.
     * @param criteriaObjects The list of criteria values as extracted from the database.
     * @return The criteria values in the string representation.
     */
    public List<String> convertCriteriaValues(List<?> criteriaObjects);

}
