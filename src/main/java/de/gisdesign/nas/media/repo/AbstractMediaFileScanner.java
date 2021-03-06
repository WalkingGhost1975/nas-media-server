package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileData;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.MediaRootDirectory;
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
    private MediaFileLibraryManager mediaFileLibraryManager;

    @Override
    public MediaFileType getMediaFileType() {
        return getMediaRepository().getSupportedMediaFileType();
    }

    public void scanMediaFileLibrary()  {
        List<String> libraryNames = mediaFileLibraryManager.getMediaFileLibraryNames(getMediaFileType());
        LOG.info("Scanning [{}] MediaFileLibraries of type [{}].", libraryNames.size(), getMediaFileType());
        for (String libraryName : libraryNames) {
            MediaFileLibrary mediaFileLibrary = mediaFileLibraryManager.getMediaFileLibrary(getMediaFileType(), libraryName);
            LOG.info("Scanning [{}] root directories of MediaFileLibrary [{}] of type [{}]", mediaFileLibrary.getRootDirectories().size(), mediaFileLibrary.getName(), getMediaFileType());
            scanMediaFileLibrary(mediaFileLibrary);
        }
    }

    @Override
    public void scanMediaFileLibrary(MediaFileLibrary mediaFileLibrary) {
        Validate.notNull(mediaFileLibrary, "MediaFileLibrary is null.");
        List<MediaRootDirectory> rootDirectories = mediaFileLibrary.getRootDirectories();
        //Recursively traverse the root directories and scan for media files.
        for (MediaRootDirectory rootDirectory : rootDirectories) {
            synchronizeDirectory(rootDirectory.getDirectory());
        }
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
    private void synchronizeDirectory(File directory) {
        LOG.debug("Synchronizing media files of type [{}] files in directory [{}]", getMediaFileType(), directory.getAbsolutePath());
        synchronizeMediaFiles(directory);
        File[] subDirectories = directory.listFiles(directoryFilter);
        for (int i = 0; i < subDirectories.length; i++) {
            File subDirectory = subDirectories[i];
            synchronizeDirectory(subDirectory);
        }
    }
    /**
     * Synchronizes the media files in the specified directory against the {@link MediaRepository}
     * backing database.
     * @param syncId The ID of the synchronization run.
     * @param directory The directory to synchronize.
     */
    private void synchronizeMediaFiles(File directory) {
        MediaRepository<M> mediaRepository = getMediaRepository();
        Map<String, M> mediaFileDataMap = mediaRepository.loadMediaFilesFromDirectory(directory);
        File[] mediaFiles = directory.listFiles(mediaFileFilter);
        LOG.debug("Identified [{}] potential media files in directory [{}].", mediaFiles.length, directory.getAbsolutePath());
        for (int i = 0; i < mediaFiles.length; i++) {
            File mediaFile = mediaFiles[i];
            try {
                if (mediaFileDataMap.containsKey(mediaFile.getName())) {
                    LOG.debug("Updating MediaFileData for media file [{}].", mediaFile.getAbsolutePath());
                    M mediaFileData = mediaFileDataMap.remove(mediaFile.getName());
                    mediaRepository.updateMediaFileData(mediaFileData);
                } else if (mediaRepository.isSupportedMediaFile(mediaFile)) {
                    LOG.debug("Creating MediaFileData for media file [{}].", mediaFile.getAbsolutePath());
                    mediaRepository.createMediaFileData(mediaFile);
                } else {
                    LOG.info("Skipping MediaFile [{}] in directory [{}]: Unsupported file type.", mediaFile.getName(), directory.getAbsolutePath());
                }
            } catch (MediaFileScanException ex)  {
                LOG.error("Error while scanning MediaFile [" + mediaFile.getName() + "] in directory [" + directory.getAbsolutePath() + "]", ex);
            }
        }
        //Delete remaining media data entries.
        for (M mediaFileData : mediaFileDataMap.values()) {
            LOG.debug("Deleting MediaFileData for media file [{}] in directory [{}].", mediaFileData.getFilename(), mediaFileData.getAbsolutePath());
            mediaRepository.deleteMediaFileData(mediaFileData);
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
