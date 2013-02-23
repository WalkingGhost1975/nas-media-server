package de.gisdesign.nas.media.domain.catalog;

import de.gisdesign.nas.media.domain.MetaDataCriteria;
import java.util.LinkedList;
import java.util.List;

/**
 * A hierarchy of IDs of {@link MetaDataCriteria}s. Provides concept of a FIFO
 * hierarchy. This hierachy is used to build up the hierarchy of {@link CriteriaFolderCatalogEntry}s.
 * @author Denis Pasek
 */
public class CriteriaFolderHierachy {

    private List<String> criteriaIds = new LinkedList<String>();

    public CriteriaFolderHierachy() {
    }

    private CriteriaFolderHierachy(List<String> criteriaIds) {
        this.criteriaIds = criteriaIds;
    }

    /**
     * Adds criteria to the hierarchy.
     * @param criteriaId The ID of the {@link MetaDataCriteria} to add.
     */
    public void addCriteria(String criteriaId)  {
        criteriaIds.add(criteriaId);
    }

    public String getCriteria()  {
        String criteria = null;
        if (criteriaIds.size() > 0) {
            criteria = criteriaIds.get(0);
        }
        return criteria;
    }

    /**
     * Removes the first criteria from the hierarchy.
     * @return The ID of the first {@link MetaDataCriteria} in the hierary.
     */
    public CriteriaFolderHierachy getSubCriteria() {
        List<String> subCriteriaList = new LinkedList<String>(this.criteriaIds);
        if (subCriteriaList.size() > 0) {
            subCriteriaList.remove(0);
        }
        return new CriteriaFolderHierachy(subCriteriaList);
    }

    public boolean hasSubCriteria()  {
        return !criteriaIds.isEmpty();
    }
}
