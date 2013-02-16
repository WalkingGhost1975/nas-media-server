package de.gisdesign.nas.media.domain.audio;

import de.gisdesign.nas.media.domain.MediaFileMetaData;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Denis Pasek
 */
@Embeddable
public class AudioMetaData implements MediaFileMetaData, Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;

    @Column(name="ARTIST", length=255)
    private String artist;

    @Column(name="ALBUM_ARTIST", length=255)
    private String albumArtist;

    @Column(name="COMPOSER", length=255)
    private String composer;

    @Column(name="ALBUM", length=255)
    private String album;

    @Column(name="TITLE", length=255)
    private String title;

    @Column(name="GENRE", length=64)
    private String genre;

    @Column(name="TRACK_NO")
    private Integer trackNumber;

    @Column(name="DURATION")
    private Integer duration;

    @Column(name="RELEASE_YEAR")
    private Integer year;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


}
