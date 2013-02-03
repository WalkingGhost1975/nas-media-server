package de.gisdesign.nas.media.rest;

import de.gisdesign.nas.media.domain.catalog.CatalogEntry;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST resource representing a {@link CatalogEntry} being a folder.
 * @author Denis Pasek
 */
public class CatalogEntryFolderResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CatalogEntryFolderResource.class);

    /**
     * Comparator used to sort nodes, that folders are always at the beginning of the result list and all
     * nodes of same type are ordered alphabetically.
     */
    private static final NodeComparator COMPARATOR = new NodeComparator();

    /**
     * The REST {@link UriInfo} for this resource.
     */
    private UriInfo uriInfo;

    /**
     * The wrapped {@link CatalogEntry}.
     */
    private CatalogEntry catalogEntry;

    /**
     * The {@link CatalogEntryResourceBuilder}.
     */
    private CatalogEntryResourceBuilder<?> resourceBuilder;

    /**
     * Constructor.
     * @param resourceBuilder The used {@link CatalogEntryResourceBuilder}.
     * @param catalogEntry The wrapped {@link CatalogEntry}.
     * @param uriInfo The REST {@link UriInfo} for this resource.
     */
    public CatalogEntryFolderResource(CatalogEntryResourceBuilder<?> resourceBuilder, CatalogEntry catalogEntry, UriInfo uriInfo) {
        Validate.notNull(resourceBuilder, "CatalogEntryResourceBuilder is null.");
        Validate.notNull(catalogEntry, "CatalogEntry is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        this.resourceBuilder = resourceBuilder;
        this.catalogEntry = catalogEntry;
        this.uriInfo = uriInfo;
    }

    public CatalogEntry getCatalogEntry() {
        return catalogEntry;
    }

    /**
     * Retrieves the sub resource with the given name from this resource. Creates a new resource
     * based on the type of the {@link CatalogEntry}:
     * <ul>
     * <li>If {@link CatalogEntry#isFolder()} is <code>true</code> the creation of sub resource is delegated to template method
     * {@link #buildFolderResource(de.gisdesign.nas.media.domain.CatalogEntry, javax.ws.rs.core.UriInfo) }</li>
     * <li>If {@link CatalogEntry#isFolder()} is <code>false</code> the creation of sub resource is delegated to template method
     * {@link #buildMediaCatalogEntryResource(de.gisdesign.nas.media.domain.CatalogEntry, javax.ws.rs.core.UriInfo)</li>
     * </ul>
     * @param resourceName The name of the sub resources.
     * @return The sub resource or <code>null</code> if not found.
     */
    @Path("/{name}")
    public Object getSubResource(@PathParam("name") String resourceName) {
        Object subResource;
        LOG.debug("Retrieving CatalogEntry [{}] as sub resource from of CatalogEntry [{}].", resourceName, catalogEntry.getPath());
        if (catalogEntry.hasChild(resourceName))  {
            CatalogEntry subCatalogEntry = catalogEntry.getChild(resourceName);
            if (subCatalogEntry.isFolder())  {
                subResource = new CatalogEntryFolderResource(resourceBuilder, subCatalogEntry, uriInfo);
                LOG.debug("CatalogEntry [{}] as sub resource from of CatalogEntry [{}] created as folder resource.", resourceName, catalogEntry.getPath());
            } else {
                subResource = resourceBuilder.buildMediaFileResource(subCatalogEntry, uriInfo);
                LOG.debug("CatalogEntry [{}] as sub resource from of CatalogEntry [{}] created as media resource.", resourceName, catalogEntry.getPath());
            }
        } else {
            LOG.debug("CatalogEntry [{}] as sub resource from of CatalogEntry [{}] not found.", resourceName, catalogEntry.getPath());
            subResource = null;
        }
        return subResource;
    }

    /**
     * Creates the {@link Node} DTOs for a resource wrapping a {@link CatalogEntry}
     * representing an folder.
     * The {@link Node} creation depends on the type of the {@link CatalogEntry}:<br/>
     * <ul>
     * <li>If {@link CatalogEntry#isFolder()} is <code>true</code> the creation a DTO of type {@link Folder} is created</li>
     * <li>If {@link CatalogEntry#isFolder()} is <code>false</code> the creation of sub resource is delegated to template method
     * {@link #buildMediaCatalogEntryNode(de.gisdesign.nas.media.domain.CatalogEntry, java.lang.String)</li>
     * </ul>
     * @return The list of {@link Node}s representing different types of {@link CatalogEntry}s.
     */
    @GET
    @Produces("application/json; charset=UTF-8")
    public List<NodeDTO> getCatalogEntries() {
        List<NodeDTO> nodes = new LinkedList<NodeDTO>();
        if (catalogEntry.isFolder())  {
            LOG.debug("Creating Nodes of Folder [{}].", catalogEntry.getPath());
            List<CatalogEntry> subEntries = catalogEntry.getChildren();
            for (CatalogEntry subEntry: subEntries) {
                long start = System.currentTimeMillis();
                String uri = uriInfo.getAbsolutePathBuilder().path(subEntry.getName()).build().toString();
                if (subEntry.isFolder())  {
                    nodes.add(new FolderDTO(subEntry.getCategory(), subEntry.getName(), uri, subEntry.size()));
                    LOG.debug("Created SubFolder [{}] in Folder [{}] in [{}ms].", subEntry.getName(), catalogEntry.getPath(), (System.currentTimeMillis() - start));
                } else {
                    nodes.add(resourceBuilder.buildMediaFile(subEntry, uriInfo));
                    LOG.debug("Created MediaNode [{}] in Folder [{}].", subEntry.getName(), catalogEntry.getPath());
                }
            }
        }
        //Always sort result
        Collections.sort(nodes, COMPARATOR);
        return nodes;
    }

    /**
     * Comparator capable of sorting {@link Node}s based on type and name. Folders
     * are always sorted on to the beginning in alphabetical orders followed by the
     * media entries in alphabetical order.
     */
    private static final class NodeComparator implements Comparator<NodeDTO> {
        @Override
        public int compare(NodeDTO o1, NodeDTO o2) {
            int result;
            if (o1 instanceof FolderDTO)  {
                result = -1;
                if (o2 instanceof FolderDTO)  {
                    result = o1.getName().compareTo(o2.getName());
                }
            } else if (o2 instanceof FolderDTO) {
                result = 1;
            } else {
                result = o1.getName().compareTo(o2.getName());
            }
            return result;
        }
    }
}
