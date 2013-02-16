package de.gisdesign.nas.media.rest.audio;

import de.gisdesign.nas.media.rest.MediaFileDTO;

/**
 * REST service DTO for Audio File resources.
 * @author Denis PasekS
 */
public class AudioDTO extends MediaFileDTO {

    private String artist;

    private String albumArtist;

    private String composer;

    private String album;

    private String title;

    private String genre;

    private Integer trackNumber;

    private Integer duration;

    private Integer year;

    public AudioDTO() {
    }

    public AudioDTO(String name, String uri) {
        super(name, uri);
    }

    public AudioDTO(String name, String uri, Long lastModified, Long size) {
        super(name, uri, lastModified, size);
    }

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
