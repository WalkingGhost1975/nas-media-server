package de.gisdesign.nas.media.rest.audio;

import de.gisdesign.nas.media.domain.audio.AudioFileData;
import de.gisdesign.nas.media.domain.audio.AudioMetaData;
import de.gisdesign.nas.media.repo.audio.AudioMediaRepository;
import de.gisdesign.nas.media.rest.MediaFileDTO;
import java.io.File;
import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAX-RS REST resource representing an audio file managed my the {@link AudioMediaRepository}.
 * @author Denis Pasek
 */
public class AudioFileResource {

    private static final Logger LOG = LoggerFactory.getLogger(AudioFileResource.class);

    private UriInfo uriInfo;
    private AudioFileData audioFileData;
    private AudioResourceBuilder resourceBuilder;

    public AudioFileResource(AudioResourceBuilder resourceBuilder, AudioFileData audioFileData, UriInfo uriInfo) {
        Validate.notNull(resourceBuilder, "AudioResourceBuilder is null.");
        Validate.notNull(audioFileData, "AudioFileData is null.");
        Validate.notNull(uriInfo, "UriInfo is null.");
        this.resourceBuilder = resourceBuilder;
        this.audioFileData = audioFileData;
        this.uriInfo = uriInfo;
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    public MediaFileDTO getAudioNode() {
        String audioId = String.valueOf(audioFileData.getId());
        String uri = uriInfo.getBaseUriBuilder().path("/audio/file").path(audioId).build().toString();
        return resourceBuilder.buildMediaFile(audioFileData, uriInfo, uri);
    }

    @GET
    @Path("/metadata")
    @Produces("application/json; charset=UTF-8")
    public AudioMetaData getAudioDetails() {
        return audioFileData.getMetaData();
    }

    @GET
    @Path("/download")
    @Produces("audio/*")
    public Response getDownload() {
        File downloadFile = new File(audioFileData.getAbsolutePath(), audioFileData.getFilename());
        return createAudioResponse(downloadFile);
    }

    /**
     * Creates the REST response for an audio file.
     * @param audioFile The audio file.
     * @return The REST {@link Response}.
     * @throws WebApplicationException If the fiel does not exist.
     */
    private Response createAudioResponse(File audioFile) throws WebApplicationException {
        if (audioFile == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        String mt = new MimetypesFileTypeMap().getContentType(audioFile);
        LOG.debug("Delivering Audio file [{}] with content type [{}].", audioFile.getAbsolutePath(), mt);
        return Response.ok(audioFile, mt).header("Content-Disposition", "inline; filename=" + audioFile.getName()).build();
    }
}
