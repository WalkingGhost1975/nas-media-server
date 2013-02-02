package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.image.ImageFileData;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pasekdbh
 */
public class ThumbGeneratorTest {

    private ThumbGenerator thumbGenerator = new ThumbGenerator();

    @Test
    public void testRescaleImage() {
        File inputFile1 = new File("src/test/resources/portrait.jpg");
        ImageFileData imageData1 = new ImageFileData();
        imageData1.setAbsolutePath(inputFile1.getParent());
        imageData1.setFilename(inputFile1.getName());

        File outputDirectory = new File("target");
        thumbGenerator.generateScaledImages(imageData1, outputDirectory, 1080, 256, 128);

        File inputFile2 = new File("src/test/resources/landscape.jpg");
        ImageFileData imageData2 = new ImageFileData();
        imageData2.setAbsolutePath(inputFile2.getParent());
        imageData2.setFilename(inputFile2.getName());
        thumbGenerator.generateScaledImages(imageData2, outputDirectory, 1080, 256, 128);
    }
}
