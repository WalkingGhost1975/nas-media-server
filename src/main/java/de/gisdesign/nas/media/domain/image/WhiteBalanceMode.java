package de.gisdesign.nas.media.domain.image;

/**
 *
 * @author Denis Pasek
 */
public enum WhiteBalanceMode {
    AUTO, MANUAL;

    /**
     * Factory method to retrieve {@link WhiteBalanceMode} based on EXIF directory value.
     * @param value The EXIF value.
     * @return The {@link WhiteBalanceMode} or <code>null</code> if unknown.
     */
    public static WhiteBalanceMode byExifCode(Integer value)  {
        if (value != null && value >= 0 && value <= 1)  {
            return values()[value];
        } else {
            return null;
        }
    }
}
