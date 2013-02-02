package de.gisdesign.nas.media.domain.image;

import de.gisdesign.nas.media.domain.MediaFileData;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Denis Pasek
 */
@Entity
@Table(name="IMAGE_FILE")
public class ImageFileData implements MediaFileData, Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Column(name="SYNC_ID")
    private Long syncId;

    @Column(name="LOCATION", length=1024)
    private String absolutePath;

    @Column(name="FILENAME")
    private String filename;

    @Column(name="RATING")
    private Integer rating;

    @Column(name="LAST_MODIFIED")
    private Long lastModified;

    @Embedded
    private ImageMetaData metaData = new ImageMetaData();

    @OneToMany(fetch= FetchType.EAGER, cascade= CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="IMAGE_ID")
    private Set<ImageTag> tags = new TreeSet<ImageTag>();

    public Long getId() {
        return id;
    }

    public Long getSyncId() {
        return syncId;
    }

    @Override
    public void setSyncId(Long syncId) {
        this.syncId = syncId;
    }

    @Override
    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public boolean hasChanged(long modificationTimestamp)  {
        return (this.lastModified != null) ? (modificationTimestamp != this.lastModified) : true;
    }

    /**
     * Generates the abstract file pointing at the slideshow image. It does NOT generate the image itself.
     * @param outputDirectory The base output directory for slideshow files.
     * @param resolution The target resolution of the slideshow file.
     * @return The abstract {@link File} targeting the slideshow file.
     */
    public File createSlideShowFileForResolution(File outputDirectory, int resolution) {
        String slideshowFilename = generateFileName("sl_" + resolution + "_");
        return new File(getRescaleOutputDirectory(outputDirectory), slideshowFilename);
    }

    /**
     * Generates the abstract file pointing at the thumbnail image. It does NOT generate the image itself.
     * @param outputDirectory The base output directory for thumbnail files.
     * @param resolution The target resolution of the thumbnail image.
     * @return The abstract {@link File} targeting the thumbnail image.
     */
    public File createThumbNailFileForResolution(File outputDirectory, int resolution) {
        String thumbFilename = generateFileName("th_" + resolution + "_");
        return new File(getRescaleOutputDirectory(outputDirectory), thumbFilename);
    }

    public ImageMetaData getMetaData() {
        if (metaData == null)  {
            this.metaData = new ImageMetaData();
        }
        return metaData;
    }

    @Override
    public Integer getRating() {
        return rating;
    }

    @Override
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void addTag(String text)  {
        ImageTag imageTag = new ImageTag();
        imageTag.setText(text);
        this.tags.add(imageTag);
    }

    public Set<ImageTag> getTags()  {
        return Collections.unmodifiableSet(this.tags);
    }

    public void setTags(Set<ImageTag> tags)  {
        this.tags = (tags != null) ? new TreeSet<ImageTag> (tags) : new TreeSet<ImageTag>();
    }

    /**
     * Generates the sub directory for the slideshow/thumbfile.
     * @param outputDirectory The base output directory.
     * @return The target output directory.
     */
    private File getRescaleOutputDirectory(File outputDirectory)  {
        Validate.notNull(outputDirectory, "Output directory is null.");
        Validate.isTrue(outputDirectory.exists(), "Output directory does not exist.");
        Validate.isTrue(outputDirectory.isDirectory(), "Output directory is not a directory.");
        Validate.isTrue(outputDirectory.canWrite(), "Output directory is not writeable.");
        String uuid = generateUUID();
        File rescaleDirectory = outputDirectory;
        for (int i = 0; i < 3; i++)  {
            rescaleDirectory = new File(rescaleDirectory, String.valueOf(uuid.charAt(i)));
            if (!rescaleDirectory.exists())  {
                rescaleDirectory.mkdir();
            }
        }
        return rescaleDirectory;
    }

    /**
     * Generates a unique file name for resclaed images based on the original filename.
     * @param prefix The prefix to use. Should mark the type of image (e.g. sl, th) and should contain the size.
     * @param file The original image file.
     * @return The generated file name.
     */
    private String generateFileName(String prefix)  {
        StringBuilder sb = new StringBuilder(255);
        sb.append(prefix).append('_');
        sb.append(generateUUID());
        int extensionIndex = getFilename().lastIndexOf('.');
        String extension = getFilename().substring(extensionIndex).toLowerCase();
        sb.append(extension);
        return sb.toString();
    }

    /**
     * Generates a UUID based on the image meta data. Used for thumbnail generation.
     * This way the same thumb can be reused even if the file has been moved in the file system.
     * @return The unique ID for the image file.
     */
    private String generateUUID()  {
        StringBuilder sb = new StringBuilder(255);
        File directory = new File(getAbsolutePath());
        sb.append(directory.getName()).append("_").append(getFilename());

        //Use metadata to make ID unique
        if (getMetaData().getCreationDate() != null)  {
            sb.append('_').append(getMetaData().getCreationDate().getTime());
        }
        if (getMetaData().getWidth() != null)  {
            sb.append('_').append(getMetaData().getWidth());
        }
        if (getMetaData().getHeight() != null)  {
            sb.append('_').append(getMetaData().getHeight());
        }
        ImageCameraData cameraData = getMetaData().getCameraData();
        if (cameraData.getManufacturer() != null)  {
            sb.append('_').append(cameraData.getManufacturer());
        }
        if (cameraData.getModel() != null)  {
            sb.append('_').append(cameraData.getModel());
        }
        return DigestUtils.md5Hex(sb.toString());
    }
}
