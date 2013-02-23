package de.gisdesign.nas.media.repo.catalog;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import de.gisdesign.nas.media.repo.MediaFileScanException;
import de.gisdesign.nas.media.repo.MediaRepository;
import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link CatalogEntry} implementation based on a directory in the filesystem.
 * @param <M> The supported type of {@link MediaFileData}.
 * @author Denis Pasek
 */
public class MediaDirectoryCatalogEntry<M extends MediaFileData> implements CatalogEntry {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MediaDirectoryCatalogEntry.class);

    /**
     * The media repository service.
     */
    private MediaRepository<M> mediaRepository;

    /**
     * The directory {@link File}.
     */
    private File file;

    /**
     * The parent {@link MediaDirectoryCatalogEntry}.
     */
    private MediaDirectoryCatalogEntry<M> parent;

    /**
     * The loaded {@link MediaFileData} meta information of all media files in the directory.
     */
    private Map<String,M> mediaFileDataMap;

    /**
     * The path in the navigation structure.
     */
    private String path;

    /**
     * Constructor.
     * @param mediaRepository The {@link MediaRepositoryService}. May not be <code>null</code>.
     * @param parent The parent {@link MediaDirectoryCatalogEntry}. Maybe <code>null</code>.
     * @param file The wrapped directory as {@link File}. May not be <code>null</code>.
     */
    public MediaDirectoryCatalogEntry(MediaRepository<M> mediaRepository, MediaDirectoryCatalogEntry<M> parent , File file) {
        LOG.debug("Creating MediaDirectoryCatalogEntry for directory [{}].", file.getAbsolutePath());
        this.mediaRepository = mediaRepository;
        this.file = file;
        this.parent = parent;
        this.path = (parent != null) ? parent.getPath() + "/" + file.getName() : file.getName();
        LOG.debug("Created MediaDirectoryCatalogEntry for directory [{}] successfully.", file.getAbsolutePath());
    }

    @Override
    public String getCategory() {
        return "directory";
    }

    @Override
    public String getName() {
        return file.getName();
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
        return file.isDirectory();
    }

    @Override
    public long size() {
        long start = System.currentTimeMillis();
        File[] children = file.listFiles();
        int size = 0;
        for (int i = 0; i < children.length; i++) {
            File child = children[i];
            if (child.isDirectory() || mediaRepository.isSupportedMediaFile(child))  {
                size++;
            }
        }
        LOG.debug("Counted [{}] child entries in directory [{}] in [{}ms]", size, file.getAbsolutePath(), (System.currentTimeMillis() - start));
        return size;
    }

    @Override
    public List<CatalogEntry> getChildren() {
        LOG.debug("Assembling children MediaDirectoryCatalogEntry for directory [{}].", file.getAbsolutePath());
        List<CatalogEntry> children = null;
        File[] childFiles = file.listFiles();
        if (childFiles != null)  {
            children = new ArrayList<CatalogEntry>(childFiles.length);
            for (int i = 0; i < childFiles.length; i++) {
                File childFile = childFiles[i];
                CatalogEntry catalogEntry = getChild(childFile.getName());
                if (catalogEntry != null)  {
                    children.add(catalogEntry);
                }
            }
        }
        LOG.debug("Assembled children MediaDirectoryCatalogEntry for directory [{}] successfully.", file.getAbsolutePath());
        return children;
    }

    @Override
    public boolean hasChild(String name) {
        File childFile = new File(file, name);
        return childFile.exists();
    }

    @Override
    public CatalogEntry getChild(String name) {
        CatalogEntry catalogEntry = null;
        File childFile = new File(file, name);
        if (childFile.exists())  {
            LOG.debug("Retrieving child [{}] MediaDirectoryCatalogEntry in directory [{}].", childFile.getName(), file.getAbsolutePath());
            if (childFile.isDirectory())  {
                catalogEntry = new MediaDirectoryCatalogEntry<M>(mediaRepository, this, childFile);
            } else {
                M metaData = getMediaFileData(childFile.getName());
                if (metaData != null)  {
                    LOG.debug("Re-Using existing MediaFileData for MediaFile [{}] in directory [{}].", childFile.getName(), file.getAbsolutePath());
                    catalogEntry = mediaRepository.createMediaFileCatalogEntry(this, metaData);
                } else if (mediaRepository.isSupportedMediaFile(childFile)) {
                    LOG.debug("Creating MediaFileData for MediaFile [{}] in directory [{}].", childFile.getName(), file.getAbsolutePath());
                    try {
                        metaData = mediaRepository.createMediaFileData(childFile);
                        catalogEntry = mediaRepository.createMediaFileCatalogEntry(this, metaData);
                    } catch (MediaFileScanException ex)  {
                        LOG.error("Error while scanning MediaFile [" + childFile.getName() + "] in directory [" + file.getAbsolutePath() + "]", ex);
                    }
                } else {
                    LOG.info("Skipping MediaFile [{}] in directory [{}]: Unsupported file type.", childFile.getName(), file.getAbsolutePath());
                }
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
    private M getMediaFileData(String filename)  {
        if (this.mediaFileDataMap == null)  {
            initializeMediaFileDataMap();
        }
        return this.mediaFileDataMap.get(filename);
    }

    /**
     * Initializes the cached media file information for the supported media files
     * in the underlying media file directory.
     */
    private void initializeMediaFileDataMap() {
        this.mediaFileDataMap = mediaRepository.loadMediaFilesFromDirectory(file);
    }

}
