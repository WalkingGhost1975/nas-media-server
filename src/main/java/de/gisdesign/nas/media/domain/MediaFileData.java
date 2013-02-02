package de.gisdesign.nas.media.domain;

/**
 * Interface describing the common basic information about a media file.
 * @author Denis Pasek
 */
public interface MediaFileData {

    /**
     * Sets the sync ID. Files with invalid sync ID will be deleted after synchronization run.
     * @param syncId
     */
    public void setSyncId(Long syncId);

    /**
     * Retrurns the absolute path of the media file directory the media file can be found.
     * @return The location of the media file.
     */
    public String getAbsolutePath();

    /**
     * Returns the file name of the media file.
     * @return The file name.
     */
    public String getFilename();

    /**
     * Returns the last modification timestamp of the media file.
     * @return The last modification timestamp.
     */
    public Long getLastModified();

    /**
     * Method to check if the media file has been touched since the last meta data scan.
     * @param modificationTimestamp The current modification timestamp of the file on disk.
     * @return <code>true</code> if file has been modified.
     */
    public boolean hasChanged(long modificationTimestamp);

    /**
     * Retrieves the personal rating of this media file between 1 = bad and 5 = excellent.
     * @return Te personal rating.
     */
    public Integer getRating();

    /**
     * Sets the personal rating for this media file.
     * @param rating The personal rating between 1 = bad and 5 = excellent.
     */
    public void setRating(Integer rating);
}
