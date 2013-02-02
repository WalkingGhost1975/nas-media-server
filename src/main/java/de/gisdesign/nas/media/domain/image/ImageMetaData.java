package de.gisdesign.nas.media.domain.image;

import de.gisdesign.nas.media.domain.MediaFileMetaData;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Denis Pasek
 */
@Embeddable
public class ImageMetaData implements MediaFileMetaData, Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    @Column(name="WIDTH")
    private Integer width;

    @Column(name="HEIGHT")
    private Integer height;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="IMAGE_DATE")
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name="COLOR_SPACE")
    private ColorSpace colorSpace;

    /**
     * The image source data.
     */
    private ImageSourceData sourceData = new ImageSourceData();

    /**
     * The camera data.
     */
    private ImageCameraData cameraData = new ImageCameraData();

    /**
     * The exposure data.
     */
    private ImageExposureData exposureData = new ImageExposureData();

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Date getCreationDate() {
        return (creationDate != null) ? new Date(creationDate.getTime()) : null;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = (creationDate != null) ? new Date(creationDate.getTime()) : null;
    }

   public ColorSpace getColorSpace() {
        return colorSpace;
    }

    public void setColorSpace(ColorSpace colorSpace) {
        this.colorSpace = colorSpace;
    }

    public ImageSourceData getSourceData() {
        if (sourceData == null)  {
            this.sourceData = new ImageSourceData();
        }
        return sourceData;
    }

    public ImageExposureData getExposureData() {
        if (exposureData == null)  {
            this.exposureData = new ImageExposureData();
        }
        return exposureData;
    }

    public ImageCameraData getCameraData() {
        if (cameraData == null)  {
            this.cameraData = new ImageCameraData();
        }
        return cameraData;
    }
}
