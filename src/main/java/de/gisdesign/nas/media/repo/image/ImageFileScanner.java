package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.AbstractMediaFileScanner;
import de.gisdesign.nas.media.repo.MediaFileScanner;
import de.gisdesign.nas.media.repo.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link MediaFileScanner} implementation for {@link ImageFileData} based on the
 * {@link AbstractMediaFileScanner}.
 * @author Denis Pasek, Senacor Technologies AG
 */
@Component
public class ImageFileScanner extends AbstractMediaFileScanner<ImageFileData> {

    @Autowired
    private ImageMediaRepository mediaRepository;

    @Override
    public void scanMediaFileLibrary(MediaFileLibrary mediaFileLibrary) {
        //Execute image scanning
        super.scanMediaFileLibrary(mediaFileLibrary);
        //Trigger image rescaling afterwards
        mediaRepository.generateRescaledImageFiles();
    }

    @Override
    protected MediaRepository<ImageFileData> getMediaRepository() {
        return mediaRepository;
    }
}
