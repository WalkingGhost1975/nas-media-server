package de.gisdesign.nas.media.domain.catalog;

import java.security.Principal;
import java.util.List;

/**
 * Generic interface for managing the structure of media catalogs. The catalogs consist
 * of nested catalog entries. The navigation structure is handled by the concrete implementations and
 * can be based on a file system structure or meta data based structures.
 * @author Denis Pasek
 */
public interface CatalogEntry {

    /**
     * Returns the category of the {@link CatalogEntry}.
     * @return The category of this entry.
     */
    public String getCategory();

    /**
     * Returns the name of the catalog entry.
     * @return The name of this entry.
     */
    public String getName();

    /**
     * Returns the path of this entry upto the root {@link CatalogEntry}.
     * @return The path of this {@link CatalogEntry}.
     */
    public String getPath();

    /**
     * Retrieves the parent {@link CatalogEntry}. Maybe null if this element is a root element.
     * @return The parent {@link CatalogEntry}.
     */
    public CatalogEntry getParent();

    /**
     * Convenience method to identify subfolders and leaf entries in the catalog.
     * @return <code>true</code> if the {@link CatalogEntry} represents a sub folder.
     */
    public boolean isFolder();

    /**
     * Returns the number of children (if any). Should only be used if
     * {@link #isFolder()} returns <code>true</code>.
     * @return The number of children.
     */
    public int size();

    /**
     * Returns all the children {@link CatalogEntry}s of this {@link CatalogEntry}. Will
     * be <code>null</code> if {@link #isFolder()} returns <code>false</code>.
     * @return The list of child {@link CatalogEntry}s.
     */
    public List<CatalogEntry> getChildren();

    /**
     * Convenience method to check if a child {@link CatalogEntry} with the given name is present.
     * @param name The name of the requested child {@link CatalogEntry}.
     * @return <code>true</code> if child {@link CatalogEntry} is present.
     */
    public boolean hasChild(String name);

    /**
     * Retrieves a child {@link CatalogEntry} by name.
     * @param name The name of the requested child {@link CatalogEntry}
     * @return The {@link CatalogEntry} or <code>null</code> if not found.
     */
    public CatalogEntry getChild(String name);

    /**
     * Checks if the specified {@link Principal} is authorized to access this {@link CatalogEntry}.
     * @param principal The authenticated {@link Principal}.
     * @return <code>true</code> if the {@link Principal} is authorized to access this {@link CatalogEntry}.
     */
    public boolean isAuthorized(Principal principal);
}
