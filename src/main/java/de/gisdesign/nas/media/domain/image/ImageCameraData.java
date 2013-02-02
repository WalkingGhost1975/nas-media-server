package de.gisdesign.nas.media.domain.image;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Denis Pasek
 */
@Embeddable
public class ImageCameraData implements Serializable {

    @Column(name="CAM_MANUFACTURER", length=64)
    private String manufacturer;

    @Column(name="CAM_MODEL", length=64)
    private String model;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
