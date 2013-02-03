package de.gisdesign.nas.media.repo;

import de.gisdesign.nas.media.domain.MediaFileLibrary;
import de.gisdesign.nas.media.domain.MediaFileType;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pasekdbh
 */
public class AbstractMediaFileScannerTest {

    private MediaFileLibraryEntity fileLibrary;

    private MockMediaRepository mediaRepository;

    private AbstractMediaFileScanner<ImageFileData> fileScanner;

    @Before
    public void setUp() {
        //Create mock media repository
        mediaRepository = new MockMediaRepository();

        //Create MediaFileLibrary
        this.fileLibrary = new MediaFileLibraryEntity(MediaFileType.IMAGE, "test");
        this.fileLibrary.addRootDirectory("test", new File("src/test/resources/media"));

        //Create MediaFileScanner
        this.fileScanner = new AbstractMediaFileScanner<ImageFileData>() {
            @Override
            protected MediaRepository<ImageFileData> getMediaRepository() {
                return mediaRepository;
            }
        };
    }

    @Test
    public void testShouldScanDirectoryWithSubDirectories() {
        this.fileScanner.scanMediaFileLibrary(fileLibrary);
        //Check is all directories have been traversed.
        File image1Directory = new File("src/test/resources/media");
        Map<String,ImageFileData> image1FileDataMap = mediaRepository.loadMediaFilesFromDirectory(image1Directory);
        assertEquals(1, image1FileDataMap.size());
        ImageFileData image1Data = image1FileDataMap.get("test-image.jpg");
        assertNotNull(image1Data);
        File image2Directory = new File("src/test/resources/media/sub1");
        Map<String,ImageFileData> image2FileDataMap = mediaRepository.loadMediaFilesFromDirectory(image2Directory);
        assertEquals(1, image2FileDataMap.size());
        ImageFileData image2Data = image2FileDataMap.get("test-image.jpg");
        assertNotNull(image2Data);
        File image3Directory = new File("src/test/resources/media/sub1/sub2");
        Map<String,ImageFileData> image3FileDataMap = mediaRepository.loadMediaFilesFromDirectory(image3Directory);
        assertEquals(1, image3FileDataMap.size());
        ImageFileData image3Data = image3FileDataMap.get("test-image.jpg");
        assertNotNull(image3Data);

        //Check if orphan deletion has been triggered
        //assertTrue(mediaRepository.isDeletedOrphans());
    }
}
