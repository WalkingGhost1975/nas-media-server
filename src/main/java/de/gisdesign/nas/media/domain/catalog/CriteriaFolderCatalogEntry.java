package de.gisdesign.nas.media.domain.catalog;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MetaDataCriteria;
import de.gisdesign.nas.media.repo.MediaRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link CatalogEntry} implementation based on {@link MetaDataCriteria}.<br/>
 * The {@link MetaDataCriteria} are assembled into a hierachical criteria structure
 * and the {@link CriteriaFolderCatalogEntry} build up the catalog structures containing
 * the media files organzed based on the {@link MetaDataCriteria}.
 * @param <M> The supported type of {@link MediaFileData}.
 * @author Denis Pasek
 */
public class CriteriaFolderCatalogEntry<M extends MediaFileData> implements CatalogEntry {

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
    private CriteriaFolderCatalogEntry<M> parent;

    /**
     * The loaded {@link MediaFileData} meta information of all media file in the directory.
     */
    private Map<String,M> mediaFileDataMap = new HashMap<String,M>();

    /**
     * The {@link MetaDataCriteria} of this folder.
     */
    private MetaDataCriteria criteria;

    /**
     * The child criteria. Only used if not being leaf folder.
     */
    private Map<String,MetaDataCriteria> childCriteriaMap = new HashMap<String, MetaDataCriteria>();

    /**
     * The path in the navigation structure.
     */
    private String path;

    /**
     * Constructor.
     * @param mediaRepository The {@link MediaRepositoryService}. May not be <code>null</code>.
     * @param parent The parent {@link CriteriaFolderCatalogEntry}. Maybe <code>null</code>.
     * @param criteria The {@link MetaDataCriteria}.
     */
    public CriteriaFolderCatalogEntry(MediaRepository<M> mediaRepository, CriteriaFolderCatalogEntry<M> parent, MetaDataCriteria criteria) {
        LOG.debug("Creating CriteriaFolderCatalogEntry for criteria [{}] with value [{}].", criteria.getName(), criteria.getValue());
        this.mediaRepository = mediaRepository;
        this.criteria = criteria;
        this.parent = parent;
        buildPath(parent);
        if (criteria.isLeaf())  {
            initializeMediaFileDataMap();
        } else {
            initializeChildCriteria();
        }
        LOG.debug("Created CriteriaFolderCatalogEntry for criteria [{}] with value [{}] successfully.", criteria.getName(), criteria.getValue());
    }

    @Override
    public String getCategory() {
        return this.criteria.getMediaFileType().getNamespace() + ":" + this.criteria.getName();
    }

    @Override
    public String getName() {
        return this.criteria.getValue();
    }

    @Override
    public String getPath() {
        return path;

    }

    @Override
    public CatalogEntry getParent() {
        return parent;
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    @Override
    public int size() {
        int size;
        if (criteria.isLeaf())  {
            size = this.mediaFileDataMap.size();
        } else {
            size = this.childCriteriaMap.size();
        }
        return size;
    }

    @Override
    public List<CatalogEntry> getChildren() {
        LOG.debug("Assembling child CriteriaFolderCatalogEntry for criteria folder [{}].", getPath());
        List<CatalogEntry> children = new ArrayList<CatalogEntry>(size());
        if (criteria.isLeaf())  {
            for (M mediaFileData : this.mediaFileDataMap.values()) {
                CatalogEntry catalogEntry = mediaRepository.createMediaFileCatalogEntry(this, mediaFileData);
                children.add(catalogEntry);
            }
        } else {
            for (MetaDataCriteria childCriteria : this.childCriteriaMap.values()) {
                CatalogEntry catalogEntry = new CriteriaFolderCatalogEntry<M>(mediaRepository, this, childCriteria);
                children.add(catalogEntry);
            }
        }
        LOG.debug("Assembled children CriteriaFolderCatalogEntry for criteria folder [{}] successfully.", getPath());
        return children;
    }

    @Override
    public boolean hasChild(String name) {
        boolean hasChild;
        if (criteria.isLeaf())  {
            hasChild = this.mediaFileDataMap.containsKey(name);
        } else {
            hasChild = this.childCriteriaMap.containsKey(name);
        }
        return hasChild;
    }

    @Override
    public CatalogEntry getChild(String name) {
        CatalogEntry catalogEntry = null;
        if (hasChild(name))  {
            LOG.debug("Retrieving child [{}] CriteriaFolderCatalogEntry for criteria folder [{}].", name, getPath());
            if (this.criteria.isLeaf())  {
                LOG.debug("Using existing MediaFileData for ImageFile [{}] for criteria folder [{}].", name, getPath());
                M metaData = getMediaFileData(name);
                catalogEntry = mediaRepository.createMediaFileCatalogEntry(this, metaData);
            } else {
                MetaDataCriteria childCriteria = this.childCriteriaMap.get(name);
                catalogEntry = new CriteriaFolderCatalogEntry<M>(mediaRepository, this, childCriteria);
            }
        }
        return catalogEntry;
    }

    @Override
    public boolean isAuthorized(Principal principal) {
        return true;
    }

    /**
     * Retrieves the {@link MediaFileData} for the specified file.
     * @param filename The file name of the media file.
     * @return The {@link MediaFileData}.
     */
    private M getMediaFileData(String absoluteFileName)  {
        return this.mediaFileDataMap.get(absoluteFileName);
    }

    /**
     * Initializes the cached media file information for the supported media files
     * in the underlying media file directory.
     */
    private void initializeMediaFileDataMap() {
        List<M> mediaFileDataList = mediaRepository.findMediaFilesByCriteria(criteria);
        LOG.debug("Loaded {} MediaFileData for media files in criteria folder [{}].", mediaFileDataList.size(), getPath());
        for (M mediaFileData : mediaFileDataList) {
            this.mediaFileDataMap.put(mediaFileData.getFilename(), mediaFileData);
        }
    }

    /**
     * Initializes the possible options for sub folders based on the {@link MetaDataCriteria}
     * of this entry.
     */
    private void initializeChildCriteria() {
        MetaDataCriteria childCriteria = (this.criteria.getValue() != null) ? this.criteria.getChildCriteria() : this.criteria;
        List<MetaDataCriteria> childCriteriaList = mediaRepository.loadMetaDataCriteriaOptions(childCriteria);
        LOG.debug("Loaded {} child MetaDataCriteria for Criteria [{}] at path [{}].", childCriteriaList.size(), childCriteria.getName(), getPath());
        for (MetaDataCriteria childMetaDataCriteria : childCriteriaList) {
            this.childCriteriaMap.put(childMetaDataCriteria.getValue(), childMetaDataCriteria);
        }
    }

    /**
     * Method to build the path of the {@link CriteriaFolderCatalogEntry}.
     * @param parent The parent {@link CriteriaFolderCatalogEntry}.
     */
    private void buildPath(CriteriaFolderCatalogEntry<M> parent) {
        String pathValue = (criteria.getValue() != null) ? criteria.getValue() : "/";
        if (parent != null)  {
            String parentPath = "/".equals(parent.getPath()) ? "" : parent.getPath();
            pathValue = parentPath + "/" + pathValue;
        }
        this.path = pathValue;
    }

}
