package de.gisdesign.nas.media.domain.image;

/**
 *
 * @author Denis Pasek
 */
public enum ColorSpace {

    sRGB, UNCALIBRATED;

    public static ColorSpace byExifCode(Integer exifValue)  {
        return Integer.valueOf(1).equals(exifValue) ? sRGB : UNCALIBRATED;
    }
}
