package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.admin.AdministrationService;
import de.gisdesign.nas.media.admin.InvalidRootDirectoryException;
import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;

/**
 *
 * @author pasekdbh
 */
public class MockAdministrationService implements AdministrationService{

    @Override
    public String getConfigurationParameter(MediaFileType mediaFileType, String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setConfigurationParameter(MediaFileType mediaFileType, String parameterName, String parameterValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MediaFileLibrary getMediaFileLibrary(MediaFileType mediaType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addMediaFileLibraryDirectory(MediaFileType mediaType, String path) throws InvalidRootDirectoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeMediaFileLibraryDirectory(MediaFileType mediaType, String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
