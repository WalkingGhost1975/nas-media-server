package de.gisdesign.nas.media.rest.audio;

import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.repo.audio.AudioFileScanner;
import de.gisdesign.nas.media.repo.audio.AudioMediaRepository;
import de.gisdesign.nas.media.rest.CatalogDTO;
import de.gisdesign.nas.media.rest.CatalogEntryResourceBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Root REST resource for managing audio files. Supports a standard audio
 * catalog model based on the file system structure and configurable catalogs
 * which use audio meta data managed in a audio meta data repository, e.g. a
 * database.
 *
 * @author pasekdbh
 */
@Component
@Path("/audio")
public class AudioRepositoryResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AudioRepositoryResource.class);
    /**
     * The {@link AudioMediaRepository}.
     */
    @Autowired
    private AudioMediaRepository audioRepository;
    /**
     * The {@link AudioFileScanner}.
     */
    @Autowired
    private AudioFileScanner audioFileScanner;

    /**
     * The {@link AudioResourceBuilder}.
     */
    private AudioResourceBuilder resourceBuilder;
    /**
     * The REST {@link UriInfo} of this resource.
     */
    @Context
    private UriInfo uriInfo;

    public AudioRepositoryResource() {
    }

    @PostConstruct
    public void init() {
        LOG.info("Initialized AudioRepository REST service!");
        resourceBuilder = new AudioResourceBuilder(audioRepository);
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<CatalogDTO> getCatalogs() {
        List<CatalogDTO> catalogs = new ArrayList<CatalogDTO>(2);
        String byFoldersUri = uriInfo.getAbsolutePathBuilder().path("byLibrary").build().toString();
        catalogs.add(new CatalogDTO("byLibrary", byFoldersUri));
        return catalogs;
    }

    /**
     * Returns the subresource for the configured audio library consisting of
     * multiple root directories. This resource will be navigable simply by the
     * filesystem directories.
     *
     * @return The REST resource representing the audio library organized by
     * filesystem directories.
     */
    @Path("/byLibrary")
    public AudioLibrariesResource getLibrary() {
        LOG.debug("Creating AudioLibrariesResource.");
        return new AudioLibrariesResource(audioRepository, uriInfo);
    }

    /**
     * Retrievs the REST resource for an audio file identified by the unique ID of the file.
     * @param id The ID of the media file.
     * @return The {@link AudioFileResource}.
     */
    @Path("/file/{id}")
    public AudioFileResource getById(@PathParam("id") Long id) {
        LOG.debug("Creating AudioFileResource.");
        AudioFileData audioFileData = audioRepository.loadMediaFileData(id);
        if (audioFileData == null)  {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new AudioFileResource(resourceBuilder, audioFileData, uriInfo);
    }

    @POST
    @Path("/scan")
    public void getScanLibrary() {
        //Scan all libraries
        LOG.info("Audio file scan manually triggered on AudioRepositoryResource.");
        this.audioFileScanner.scanMediaFileLibrary();
    }
}
