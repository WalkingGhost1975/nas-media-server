package de.gisdesign.nas.media.repo.image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import de.gisdesign.nas.media.domain.image.ImageCameraData;
import de.gisdesign.nas.media.domain.image.ImageExposureData;
import de.gisdesign.nas.media.domain.image.ImageFileData;
import de.gisdesign.nas.media.repo.MediaFileScanException;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author pasekdbh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/image-repository-test-context.xml")
public class ImageMediaRepositoryImplTest {

    private Logger LOG = LoggerFactory.getLogger(ImageMediaRepositoryImplTest.class);

    @Autowired
    private ImageMediaRepositoryImpl imageRepository;

    @Test
    public void testDumpImageMetadata() throws ImageProcessingException, IOException {
        File imageFile = new File("src/test/resources/portrait.jpg");
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                LOG.info("Directory [{}] Tag[{}] of type [{}]", tag.getDirectoryName(), tag.getTagName(), tag.getTagTypeHex());
            }
        }
    }

    @Test
    public void testShouldExtractMetaDataFromSonyImagesFiles() throws MediaFileScanException {
        File sonyFile = new File("src/test/resources/sony.jpg");
        ImageFileData imageData = imageRepository.createMediaFileData(sonyFile);
        ImageCameraData cameraData = imageData.getMetaData().getCameraData();
        assertNotNull(cameraData.getManufacturer());
        assertNotNull(cameraData.getModel());
        ImageExposureData exposureData = imageData.getMetaData().getExposureData();
        assertNotNull(exposureData.getAperture());
        assertNotNull(exposureData.getWhiteBalance());
        assertNotNull(exposureData.getIsoValue());
        assertNotNull(exposureData.getFocalLength());
        assertNotNull(exposureData.getFlashMode());
        assertNotNull(exposureData.getWhiteBalance());
    }

    @Test
    public void testShouldExtractMetaDataFromCanonImagesFiles() throws MediaFileScanException {
        File canonFile = new File("src/test/resources/portrait.jpg");
        ImageFileData imageData = imageRepository.createMediaFileData(canonFile);
        ImageCameraData cameraData = imageData.getMetaData().getCameraData();
        assertNotNull(cameraData.getManufacturer());
        assertNotNull(cameraData.getModel());
        ImageExposureData exposureData = imageData.getMetaData().getExposureData();
        assertNotNull(exposureData.getAperture());
        assertNotNull(exposureData.getWhiteBalance());
        assertNotNull(exposureData.getIsoValue());
        assertNotNull(exposureData.getFocalLength());
        assertNotNull(exposureData.getFlashMode());
        assertNotNull(exposureData.getWhiteBalance());
    }

}
