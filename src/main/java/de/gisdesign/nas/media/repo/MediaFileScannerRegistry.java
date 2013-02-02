package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileType;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek, Senacor Technologies AG
 */
@Component
public class MediaFileScannerRegistry {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MediaFileScannerRegistry.class);

    /**
     * The autowired list of all {@link MediaFileScanner}s.
     */
    @Autowired(required=false)
    private Collection<MediaFileScanner> mediaFileScanners;

    /**
     * The map of registered {@link MediaFileScanner}s.
     */
    private Map<MediaFileType, MediaFileScanner> fileScannerMap = new EnumMap<MediaFileType, MediaFileScanner>(MediaFileType.class);

    @PostConstruct
    public void initialize()  {
        //Copy MediaFileScanner to map for registration.
        if (mediaFileScanners != null)  {
            for (MediaFileScanner fileScanner : mediaFileScanners) {
                MediaFileType mediaFileType = fileScanner.getMediaFileType();
                Validate.notNull(mediaFileType, "MediaFileType of MediaFileScanner of type [" + fileScanner.getClass().getName() + "] is null.");
                if (fileScannerMap.containsKey(mediaFileType)) {
                    throw new IllegalStateException("Multiple MediaFileScanner for MediaFileType [" + mediaFileType + "] of type [" + fileScanner.getClass() + "] registered.");
                }
                fileScannerMap.put(mediaFileType, fileScanner);
            }
        }
        LOG.info("Registered {} MetaDataQueryBuilders: {}", fileScannerMap.size(), fileScannerMap.keySet());
    }

    /**
     * Retrieves a {@link MediaFileScanner} for the specified {@link MediaFileType}.
     * @param mediaFileType The {@link MediaFileType}.
     * @return The {@link MediaFileScanner} or <code>null</code> is not present.
     */
    public MediaFileScanner getMediaFileScanner(MediaFileType mediaFileType)  {
        Validate.notNull(mediaFileType, "MediaFileType is null.");
        return fileScannerMap.get(mediaFileType);
    }

}
