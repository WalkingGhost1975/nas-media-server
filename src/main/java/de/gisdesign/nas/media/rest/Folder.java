package de.gisdesign.nas.media.rest;

/**
 * {@link Node} implementation representing a folder containg subfolders and media files.
 * @author Denis Pasek
 */
public class Folder extends Node {

    private String category;

    private int count;

    public Folder() {
    }

    public Folder(String category, String name, String uri, int count) {
        super(name, uri);
        this.category = category;
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
