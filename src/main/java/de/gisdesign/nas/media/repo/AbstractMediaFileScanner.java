package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.admin.AdministrationService;
import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract implementation of the {@link MediaFileScanner} interface. Multiple
 * instanmces of this class are used to scan the different {@link MediaFileType}s.
 * @param <M> The type of {@link MediaFileData} to be processed by this {@link MediaFileScanner}.
 * @author Denis Pasek
 */
public abstract class AbstractMediaFileScanner<M extends MediaFileData> implements MediaFileScanner {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMediaFileScanner.class);

    /**
     * {@link FileFilter} to find usable media files in a directory.
     */
    private final FileFilter mediaFileFilter = new MediaFileFilter();

    /**
     * {@link FileFilter} to find usable accessible sub directories during scanning.
     */
    private final FileFilter directoryFilter = new DirectoryFilter();

    @Autowired
    private AdministrationService administrationService;

    @Override
    public MediaFileType getMediaFileType() {
        return getMediaRepository().getSupportedMediaFileType();
    }

    public void scanMediaFileLibrary()  {
        MediaFileLibrary mediaFileLibrary = administrationService.getMediaFileLibrary(getMediaFileType());
        scanMediaFileLibrary(mediaFileLibrary);
    }

    @Override
    public void scanMediaFileLibrary(MediaFileLibrary mediaFileLibrary) {
        Validate.notNull(mediaFileLibrary, "MediaFileLibrary is null.");
        List<File> rootDirectories = mediaFileLibrary.getRootDirectories();
        //Recursively traverse the root directories and scan for media files.
        Long syncId = System.currentTimeMillis();
        for (File rootDirectory : rootDirectories) {
            synchronizeDirectory(syncId, rootDirectory);
        }
        //Delete all non-synced files
        getMediaRepository().deleteOrphanedMediaFiles(syncId);
    }

    /**
     * Retrieves the underlying {@link MediaRepository} used for synchronizing the
     * media file meta data.
     * @return The {@link MediaRepository}.
     */
    protected abstract MediaRepository<M> getMediaRepository();

    /**
     * Sychronizes all media files and sub directories of the given directory against the
     * {@link MediaRepository} backing database.
     * @param syncId The ID of the synchronization run.
     * @param directory The directory to synchronize.
     */
    private void synchronizeDirectory(Long syncId, File directory) {
        LOG.info("Synchronizing media files of type [{}] files in directory [{}]", getMediaFileType(), directory.getAbsolutePath());
        synchronizeMediaFiles(syncId, directory);
        File[] subDirectories = directory.listFiles(directoryFilter);
        for (int i = 0; i < subDirectories.length; i++) {
            File subDirectory = subDirectories[i];
            synchronizeDirectory(syncId, subDirectory);
        }
    }
    /**
     * Synchronizes the media files in the specified directory against the {@link MediaRepository}
     * backing database.
     * @param syncId The ID of the synchronization run.
     * @param directory The directory to synchronize.
     */
    private void synchronizeMediaFiles(Long syncId, File directory) {
        MediaRepository<M> mediaRepository = getMediaRepository();
        Map<String, M> mediaFileDataMap = mediaRepository.loadMediaFilesFromDirectory(directory);
        File[] mediaFiles = directory.listFiles(mediaFileFilter);
        LOG.debug("Identified [{}] potential media files in directory [{}].", mediaFiles.length, directory.getAbsolutePath());
        for (int i = 0; i < mediaFiles.length; i++) {
            File mediaFile = mediaFiles[i];
            try {
                if (mediaFileDataMap.containsKey(mediaFile.getName())) {
                    LOG.debug("Updating MediaFileData for media file [{}].", mediaFile.getAbsolutePath());
                    M mediaFileData = mediaFileDataMap.get(mediaFile.getName());
                    mediaRepository.updateMediaFileData(mediaFileData, syncId);
                } else if (mediaRepository.isSupportedMediaFile(mediaFile)) {
                    LOG.debug("Creating MediaFileData for media file [{}].", mediaFile.getAbsolutePath());
                    mediaRepository.createMediaFileData(mediaFile, syncId);
                } else {
                    LOG.info("Skipping MediaFile [{}] in directory [{}]: Unsupported file type.", mediaFile.getName(), directory.getAbsolutePath());
                }
            } catch (MediaFileScanException ex)  {
                LOG.error("Error while scanning MediaFile [" + mediaFile.getName() + "] in directory [" + directory.getAbsolutePath() + "]", ex);
            }
        }
    }

    /**
     * {@link FileFilter} implementation used to find scannable media files.
     */
    private static final class MediaFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() && !pathname.isHidden() && pathname.canRead();
        }
    }

    /**
     * {@link FileFilter} implementation used to find scannable sub folders.
     */
    private static final class DirectoryFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && !pathname.isHidden() && pathname.canRead();
        }
    }

}
