package de.gisdesign.nas.media.domain.image;

/**
 *
 * @author Denis Pasek
 */
public enum FlashMode {

    NO_FLASH(0x0),
    FIRED(0x1), FIRED_RETURN_NOT_DETECTED(0x5), FIRED_RETURN_DETECTED(0x7),
    ON(0x9), ON_RETURN_NOT_DETECTED(0xd), ON_RETURN_DETECTED(0xf),
    OFF(0x10),
    AUTO_DID_NOT_FIRE(0x18), AUTO_FIRED(0x19), AUTO_RETURN_NOT_DETECTED(0x1d), AUTO_RETURN_DETECTED(0x1f),
    NO_FLASH_FUNCTION(0x20),
    FIRED_RED_EYE_REDUCTION(0x41), FIRED_RED_EYE_REDUCTION_RETURN_NOT_DETECTED(0x45), FIRED_RED_EYE_REDUCTION_RETURN_DETECTED(0x47),
    ON_RED_EYE_REDUCTION(0x49), ON_RED_EYE_REDUCTION_RETURN_NOT_DETECTED(0x4d), ON_RED_EYE_REDUCTION_RETURN_DETECTED(0x4f),
    AUTO_RED_EYE_REDUCTION_FIRED(0x59), AUTO_RED_EYE_REDUCTION_RETURN_NOT_DETECTED(0x5d), AUTO_RED_EYE_REDUCTION_RETURN_DETECTED(0x5f);

    /**
     * Factory method for retriebing flash mode, based on given EXIF directory value.
     * @param value The EXIF directory value.
     * @return The {@link FlashMode} or <code>null</code> if unknown.
     */
    public static FlashMode byExifCode(Integer value)  {
        FlashMode flashMode = null;
        for (FlashMode mode : values()) {
            if (mode.value == value) {
                flashMode = mode;
            }
        }
        return flashMode;
    }

    private int value;

    private FlashMode(int value) {
        this.value = value;
    }

}
