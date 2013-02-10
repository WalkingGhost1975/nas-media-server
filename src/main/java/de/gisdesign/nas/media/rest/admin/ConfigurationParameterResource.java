package de.gisdesign.nas.media.rest.admin;

import de.gisdesign.nas.media.admin.ConfigurationParameter;
import de.gisdesign.nas.media.admin.ConfigurationService;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.repo.MediaFileLibraryManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
 * Root REST resource wrapping the {@link MediaFileLibraryManager}.
 * @author Denis Pasek
 */
@Component
@Path("/admin")
public class ConfigurationParameterResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationParameterResource.class);
    /**
     * The {@link ConfigurationService}.
     */
    @Autowired
    private ConfigurationService configurationService;
    /**
     * The REST {@link UriInfo} of this resource.
     */
    @Context
    private UriInfo uriInfo;

    public ConfigurationParameterResource() {
    }

    @PostConstruct
    public void init() {
    }

    @GET
    @Path("/{mediaFileType}")
    @Produces("application/json; charset=UTF-8")
    public Response getConfigParams(@PathParam("mediaFileType") String mediaFileTypeString)  {
        MediaFileType mediaFileType = convertMediaFileType(mediaFileTypeString);
        List<ConfigurationParameter> configParams = configurationService.getConfigurationParameters(mediaFileType);
        Map<String,String> configParamMap = new HashMap<String, String>(configParams.size() * 2);
        for (ConfigurationParameter configurationParameter : configParams) {
            configParamMap.put(configurationParameter.getName(), configurationParameter.getValue());
        }

        return Response.ok(configParamMap).build();
    }

    @PUT
    @Path("/{mediaFileType}")
    @Produces("application/json; charset=UTF-8")
    public Response setConfigParams(@PathParam("mediaFileType") String mediaFileTypeString, Map<String,String> configParamMap)  {
        MediaFileType mediaFileType = convertMediaFileType(mediaFileTypeString);
        for (Map.Entry<String, String> entry : configParamMap.entrySet()) {
            configurationService.setConfigurationParameter(mediaFileType, entry.getKey(), entry.getValue());
        }
        return Response.ok().build();
    }

    @GET
    @Path("/{mediaFileType}/{configurationParameter}")
    @Produces("text/plain; charset=UTF-8")
    public Response getConfigParam(@PathParam("mediaFileType") String mediaFileTypeString, @PathParam("configurationParameter") String configParamName)  {
        Response.ResponseBuilder responseBuilder;
        MediaFileType mediaFileType = convertMediaFileType(mediaFileTypeString);
        String configurationValue = configurationService.getConfigurationParameter(mediaFileType, configParamName);
        if (configurationValue != null) {
            responseBuilder = Response.ok(configurationValue);
        } else {
            responseBuilder = Response.status(Response.Status.NOT_FOUND);
        }
        return responseBuilder.build();
    }

    @PUT
    @Path("/{mediaFileType}/{configurationParameter}")
    @Consumes("text/plain; charset=UTF-8")
    public Response setConfigParam(@PathParam("mediaFileType") String mediaFileTypeString, @PathParam("configurationParameter") String configParamName, String configParamValue)  {
        MediaFileType mediaFileType = convertMediaFileType(mediaFileTypeString);
        configurationService.setConfigurationParameter(mediaFileType, configParamName, configParamValue);
        return Response.created(uriInfo.getAbsolutePath()).build();
    }

    /**
     * Converts the given media type string into a {@link MediaFileType}.
     * @param mediaFileTypeString The string representation of the media file type.
     * @return The {@link MediaFileType}.
     * @throws WebApplicationException If string is an unknown {@link MediaFileType}.
     */
    private MediaFileType convertMediaFileType(String mediaFileTypeString) throws WebApplicationException {
        try {
            return MediaFileType.valueOf(mediaFileTypeString.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOG.warn("Illegal MediaFileType [" + mediaFileTypeString + "].");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
