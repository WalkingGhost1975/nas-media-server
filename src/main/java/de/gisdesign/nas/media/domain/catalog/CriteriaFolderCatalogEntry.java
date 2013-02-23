package de.gisdesign.nas.media.domain.catalog;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.domain.MetaDataCriteriaFactory;
import de.gisdesign.nas.media.repo.MediaRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link CatalogEntry} implementation based on {@link MetaDataCriteria}.<br/>
 * The {@link MetaDataCriteria} are assembled into a hierachical criteria structure
 * and the {@link CriteriaFolderCatalogEntry} build up the catalog structures containing
 * the media files organzed based on the {@link MetaDataCriteria}.
 * @param <M> The supported type of {@link MediaFileData}.
 * @param <V> The value type manged by the underlying {@link MetaDataCriteria}.
 * @author Denis Pasek
 */
public final class CriteriaFolderCatalogEntry<M extends MediaFileData,V> implements CatalogEntry {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CriteriaFolderCatalogEntry.class);

    /**
     * The media repository service.
     */
    private MediaRepository<M> mediaRepository;

    /**
     * The parent {@link CriteriaFolderCatalogEntry}.
     */
    private CriteriaFolderCatalogEntry<M,?> parent;

    /**
     * The ID of the {@link MetaDataCriteria} represented by this {@link CriteriaFolderCatalogEntry}.
     */
    private String metaDataCriteriaId;

    /**
     * The {@link CriteriaFolderHierachy}.
     */
    private CriteriaFolderHierachy childCriteriaFolderHierarchy;

    /**
     * The value string for the {@link MetaDataCriteria}.
     */
    private String value;

    /**
     * The path in the navigation structure.
     */
    private String path;

    /**
     * The {@link MetaDataCriteriaFactory}.
     */
    private MetaDataCriteriaFactory criteriaFactory;

    /**
     * The size value for the number of children.
     */
    private long size = -1;

    /**
     * Constructor.
     * @param mediaRepository The {@link MediaRepositoryService}. May not be <code>null</code>.
     * @param criteriaHierachy The {@link CriteriaFolderHierachy}.
     */
    public CriteriaFolderCatalogEntry(MediaRepository<M> mediaRepository, CriteriaFolderHierachy criteriaHierachy) {
        this(mediaRepository, null, criteriaHierachy);
    }

    /**
     * Constructor.
     * @param mediaRepository The {@link MediaRepositoryService}. May not be <code>null</code>.
     * @param parent The parent {@link CriteriaFolderCatalogEntry}. Maybe <code>null</code>.
     * @param criteriaHierachy The {@link CriteriaFolderHierachy}.
     */
    public CriteriaFolderCatalogEntry(MediaRepository<M> mediaRepository, CriteriaFolderCatalogEntry<M,?> parent, CriteriaFolderHierachy criteriaHierachy) {
        this(mediaRepository, parent, criteriaHierachy, null);
    }

    /**
     * Constructor.
     * @param mediaRepository The {@link MediaRepositoryService}. May not be <code>null</code>.
     * @param parent The parent {@link CriteriaFolderCatalogEntry}. Maybe <code>null</code>.
     * @param criteriaHierachy The {@link CriteriaFolderHierachy}.
     * @param valueString The value string for the {@link MetaDataCriteria}.
     */
    public CriteriaFolderCatalogEntry(MediaRepository<M> mediaRepository, CriteriaFolderCatalogEntry<M,?> parent, CriteriaFolderHierachy criteriaHierachy, String valueString) {
        Validate.notNull(mediaRepository, "MediaRepository is null");
        Validate.notNull(criteriaHierachy, "CriteriaFolderHierachy is null");
        this.mediaRepository = mediaRepository;
        this.criteriaFactory = mediaRepository.getMetaDataCriteriaFactory();
        this.parent = parent;
        this.metaDataCriteriaId = criteriaHierachy.getCriteria();
        this.childCriteriaFolderHierarchy = (valueString == null) ? criteriaHierachy : criteriaHierachy.getSubCriteria();
        this.value = valueString;
        this.path = buildPath();
        LOG.debug("Created CriteriaFolderCatalogEntry for criteria [{}] with value [{}].", this.metaDataCriteriaId, valueString);
    }

    @Override
    public String getCategory() {
        return this.metaDataCriteriaId;
    }

    @Override
    public String getName() {
        return this.value;
    }

    public String getValue()  {
        return this.value;
    }

    @Override
    public String getPath() {
        return path;

    }

    @Override
    public CriteriaFolderCatalogEntry<M,?> getParent() {
        return parent;
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    @Override
    public long size() {
        if (this.size < 0) {
            this.size = determineSize();
        }
        return size;
    }

    @Override
    public List<CatalogEntry> getChildren() {
        LOG.debug("Assembling child CriteriaFolderCatalogEntry for criteria folder [{}].", getPath());
        List<CatalogEntry> children;
        if (isLeaf())  {
            Map<String,M> mediaFileMap = loadMediaFileDataMap();
            children = new ArrayList<CatalogEntry>(mediaFileMap.size());
            for (M mediaFileData : mediaFileMap.values()) {
                CatalogEntry catalogEntry = mediaRepository.createMediaFileCatalogEntry(this, mediaFileData);
                if (catalogEntry != null)  {
                    children.add(catalogEntry);
                }
            }
        } else {
            return new ArrayList<CatalogEntry>(loadChildCriteriaFolders());
        }
        LOG.debug("Assembled children CriteriaFolderCatalogEntry for criteria folder [{}] successfully.", getPath());
        return children;
    }

    @Override
    public boolean hasChild(String name) {
        boolean hasChild = true;
        if (isLeaf())  {
            hasChild = loadMediaFileDataMap().containsKey(name);
        }
        return hasChild;
    }

    @Override
    public CatalogEntry getChild(String name) {
        CatalogEntry catalogEntry = null;
        LOG.debug("Retrieving child [{}] CriteriaFolderCatalogEntry for criteria folder [{}].", name, getPath());
        if (isLeaf())  {
            LOG.debug("Using existing MediaFileData for ImageFile [{}] for criteria folder [{}].", name, getPath());
            M metaData = getMediaFileData(name);
            if (metaData != null) {
                catalogEntry = mediaRepository.createMediaFileCatalogEntry(this, metaData);
            }
        } else {
            CriteriaFolderCatalogEntry<M,V> parentEntry = (value != null ? this : null);
            catalogEntry = new CriteriaFolderCatalogEntry<M,V>(mediaRepository, parentEntry, childCriteriaFolderHierarchy, name);
        }
        return catalogEntry;
    }

    @Override
    public boolean isAuthorized(Principal principal) {
        return true;
    }

    /**
     * Checks whether this {@link CriteriaFolderCatalogEntry} is the leaf of the hierachy.
     * @return <code>true</code> in case of leaf of hierarchy.
     */
    public boolean isLeaf()  {
        return (this.value != null && !this.childCriteriaFolderHierarchy.hasSubCriteria());
    }

    /**
     * Retrieves the {@link MediaFileData} for the specified file.
     * @param filename The file name of the media file.
     * @return The {@link MediaFileData}.
     */
    private M getMediaFileData(String id)  {
        return this.mediaRepository.loadMediaFileData(Long.valueOf(id));
    }

    /**
     * Initializes the cached media file information for the supported media files
     * in the underlying media file directory.
     */
    private Map<String,M> loadMediaFileDataMap() {
        Map<String,M> mediaFileDataMap = new HashMap<String, M>();
        MetaDataCriteria<?> criteria = buildMetaDataCriteriaHierarchy();
        List<M> mediaFileDataList = mediaRepository.findMediaFilesByCriteria(criteria);
        LOG.debug("Loaded {} MediaFileData for media files in criteria folder [{}].", mediaFileDataList.size(), getPath());
        for (M mediaFileData : mediaFileDataList) {
            mediaFileDataMap.put(String.valueOf(mediaFileData.getId()), mediaFileData);
        }
        return mediaFileDataMap;
    }

    /**
     * Initializes the possible options for sub folders based on the {@link MetaDataCriteria}
     * of this entry.
     */
    private List<CriteriaFolderCatalogEntry<M,?>> loadChildCriteriaFolders() {
        List<CriteriaFolderCatalogEntry<M,?>> childFolderList = new LinkedList<CriteriaFolderCatalogEntry<M,?>>();
        MetaDataCriteria<Object> criteria = buildMetaDataCriteriaHierarchy();
        List<Object> criteriaValues = mediaRepository.loadMetaDataCriteriaOptions(criteria);
        LOG.debug("Loaded {} child MetaDataCriteria for Criteria [{}] at path [{}].", criteriaValues.size(), criteria.getId(), getPath());
        for (Object criteriaValue : criteriaValues) {
            String valueString = criteria.convertToString(criteriaValue);
            CriteriaFolderCatalogEntry<M,V> parentEntry = (value != null ? this : null);
            CriteriaFolderCatalogEntry<M,?> childEntry = new CriteriaFolderCatalogEntry<M,V>(mediaRepository, parentEntry, childCriteriaFolderHierarchy, valueString);
            childFolderList.add(childEntry);
        }
        return childFolderList;
    }

    /**
     * Builds the {@link MetaDataCriteria} hierarchy used to select the child {@link CriteriaFolderCatalogEntry}s
     * or the {@link MediaFileData} from the {@link MediaRepository}.
     * @return The {@link MetaDataCriteria} hierarchy.
     */
    private MetaDataCriteria<Object> buildMetaDataCriteriaHierarchy()  {
        //Build root criteria element, depending if we are a leaf or not.
        MetaDataCriteria<Object> criteria;
        if (isLeaf()) {
            criteria = criteriaFactory.createMetaDataCriteria(metaDataCriteriaId);
            criteria.setValueAsString(value);
            addParentMetaDataCriteria(criteria, getParent());
        } else {
            criteria = criteriaFactory.createMetaDataCriteria(childCriteriaFolderHierarchy.getCriteria());
            addParentMetaDataCriteria(criteria, this);
        }
        return criteria;
    }

    /**
     * Adds the parent {@link MetaDataCriteria} to the given {@link MetaDataCriteria} by traversing the
     * parent hierarchy of the root {@link CriteriaFolderCatalogEntry}.
     * @param criteria The base {@link MetaDataCriteria} to add the parents to.
     * @param rootEntry The root {@link CriteriaFolderCatalogEntry}.
     */
    private void addParentMetaDataCriteria(MetaDataCriteria<?> criteria, CriteriaFolderCatalogEntry<M, ?> rootEntry) {
        //Add the parent elements.
        CriteriaFolderCatalogEntry<M, ?> currentEntry = rootEntry;
        MetaDataCriteria<?> currentCriteria = criteria;
        while (currentEntry != null && currentEntry.getValue() != null) {
            MetaDataCriteria<?> parentCriteria = criteriaFactory.createMetaDataCriteria(currentEntry.getCategory());
            parentCriteria.setValueAsString(currentEntry.getValue());
            currentCriteria.setParent(parentCriteria);
            currentEntry = currentEntry.getParent();
            currentCriteria = parentCriteria;
        }
    }

    /**
     * Method to build the path of the {@link CriteriaFolderCatalogEntry}.
     * @param parent The parent {@link CriteriaFolderCatalogEntry}.
     */
    private String buildPath() {
        StringBuilder pathBuilder = new StringBuilder();
        if (parent != null)  {
            String parentPath = parent.buildPath();
            pathBuilder.append("/".equals(parentPath) ? "" : parentPath);
        }
        pathBuilder.append('/');
        if (value != null) {
            pathBuilder.append(value);
        }
        return pathBuilder.toString();
    }

    /**
     * Determines the number of child elements for this criteria folder.
     * @return The number of child elements.
     */
    private long determineSize() {
        long count;
        if (isLeaf())  {
            count = mediaRepository.countMediaFilesMatchingCriteria(buildMetaDataCriteriaHierarchy());
        } else {
            count = determineNumberOfSubFolders();
        }
        LOG.debug("Determined size [{}] for Criteria folder at path [{}].", count, getPath());
        return count;
    }

    /**
     * Determines the number of sub folder if this folder entry is not a leaf.
     * @return The numer of sub folders.
     */
    private long determineNumberOfSubFolders() {
        MetaDataCriteria<Object> criteria = buildMetaDataCriteriaHierarchy();
        return mediaRepository.loadMetaDataCriteriaOptions(criteria).size();
    }
}
