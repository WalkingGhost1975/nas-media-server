package de.gisdesign.nas.media.domain.image;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author Denis Pasek
 */
@Embeddable
public class ImageExposureData implements Serializable {

    @Column(name="EXP_APERTURE")
    private Double aperture;

    @Column(name="EXP_EXPTIME")
    private Double exposureTime;

    @Column(name="EXP_ISO")
    private Integer isoValue;

    @Column(name="EXP_FOCAL_LENGTH")
    private Integer focalLength;

    @Column(name="CAM_FLASH_MODE", length=48)
    @Enumerated(EnumType.STRING)
    private FlashMode flashMode;

    @Column(name="CAM_WHITE_BALANCE", length=16)
    @Enumerated(EnumType.STRING)
    private WhiteBalanceMode whiteBalance;

    public Double getAperture() {
        return aperture;
    }

    public void setAperture(Double aperture) {
        this.aperture = aperture;
    }

    public Double getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(Double exposureTime) {
        this.exposureTime = exposureTime;
    }

    public Integer getIsoValue() {
        return isoValue;
    }

    public void setIsoValue(Integer isoValue) {
        this.isoValue = isoValue;
    }

    public Integer getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(Integer focalLength) {
        this.focalLength = focalLength;
    }

    public FlashMode getFlashMode() {
        return flashMode;
    }

    public void setFlashMode(FlashMode flashMode) {
        this.flashMode = flashMode;
    }

    public WhiteBalanceMode getWhiteBalance() {
        return whiteBalance;
    }

    public void setWhiteBalance(WhiteBalanceMode whiteBalance) {
        this.whiteBalance = whiteBalance;
    }
}
