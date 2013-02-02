package de.gisdesign.nas.media.repo.image;

import de.gisdesign.nas.media.domain.image.ImageFileData;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Denis Pasek
 */
@Component
public class ThumbGenerator {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ThumbGenerator.class);

    /**
     * User as command line tool for testing.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        File inputFile = null;
        if (args.length > 0)  {
            String inputFileName = args[0];
            inputFile = new File(inputFileName);
            if (inputFile.isDirectory() || !inputFile.exists())  {
                throw new IllegalArgumentException("Invalid input file [" + inputFileName + "].");
            }
        }
        File outputDirectory = inputFile.getParentFile();
        ThumbGenerator generator = new ThumbGenerator();
        ImageFileData imageData = new ImageFileData();
        imageData.setAbsolutePath(inputFile.getParent());
        imageData.setFilename(inputFile.getName());
        generator.generateScaledImages(imageData, outputDirectory, 1080, 256, 128);
    }

    /**
     * Method for rescaling an Image file to the requested slideshow size and thumb size.
     * Only performs resclaing if the target directory does not already contain the
     * slide show images and thumb nails.
     * @param imageData The input {@link ImageFileData}.
     * @param outputDirectory The output directory.
     * @param slideShowHeight The height to be used for images for slideshows.
     * @param thumbSizeBig The size of the big thumb nails.
     * @param thumbSizeSmall The size of small thumb nail images.
     * @return The number of resclaed images generated. Only for statistical purposes.
     */
    public int generateScaledImages(ImageFileData imageData, File outputDirectory, int slideShowHeight, int thumbSizeBig, int thumbSizeSmall) {
        int count = 0;

        File imageFile = new File(imageData.getAbsolutePath(), imageData.getFilename());
        //Prepare output files
        File slideShowFile = imageData.createSlideShowFileForResolution(outputDirectory, slideShowHeight);
        File thumbBigFile = imageData.createThumbNailFileForResolution(outputDirectory, thumbSizeBig);
        File thumbSmallFile = imageData.createThumbNailFileForResolution(outputDirectory, thumbSizeSmall);

        long start = System.currentTimeMillis();
        try {
            //Only generate rescaled versions if they do noe exis yet.
            BufferedImage rescaledImage = null;
            if (!slideShowFile.exists())  {
                rescaledImage = ImageIO.read(imageFile);
                if (rescaledImage != null)  {
                    //Rescale for slideshow
                    float scaleFactor = ((float) slideShowHeight/(float) rescaledImage.getHeight());
                    rescaledImage = performScaling(rescaledImage, slideShowFile, scaleFactor);
                    ImageIO.write(rescaledImage, "jpg", slideShowFile);
                    LOG.debug("Rescaled image [{}] to slide show resolution [{}] in [{}ms].", imageFile.getAbsolutePath(), slideShowHeight, System.currentTimeMillis() - start);
                    count++;
                }
            }

            //Created thumb nails in two sizes
            int[] sizes = new int[]{thumbSizeBig, thumbSizeSmall};
            File[] outputFiles = new File[]{thumbBigFile, thumbSmallFile};
            for (int i = 0; i < sizes.length; i++) {
                if (!outputFiles[i].exists())  {
                    start = System.currentTimeMillis();
                    //Lazy load image file if necessary
                    if (rescaledImage == null)  {
                        rescaledImage = ImageIO.read(imageFile);
                    }
                    //Calculate scaling.
                    int size = sizes[i];
                    float scaleFactor = (rescaledImage.getWidth() > rescaledImage.getHeight()) ? ((float) size/(float)rescaledImage.getWidth()) : ((float)size/(float)rescaledImage.getHeight());
                    rescaledImage = performScaling(rescaledImage, outputFiles[i], scaleFactor);
                    LOG.debug("Rescaled image [{}] to thumbnail resolution [{}] in [{}ms].", imageFile.getAbsolutePath(), size, System.currentTimeMillis() - start);
                    count++;
                }
            }
        } catch (IOException ex) {
            LOG.warn("Could process Image file [" + imageFile.getAbsolutePath() + "].", ex);
        }
        return count;
    }

    /**
     * Performs the actual scaling of the image. Calculates the new size based
     * @param inputImage The input image to be scaled.
     * @param scaleFactor The scaling factor for the target image.
     * @param outputFile The output file for the scaled image.
     * @return The sclaed image.
     * @throws IOException
     */
    private BufferedImage performScaling(BufferedImage inputImage, File outputFile, float scaleFactor) throws IOException {
        int newWidth = (int)(inputImage.getWidth() * scaleFactor);
        int newHeight = (int)(inputImage.getHeight() * scaleFactor);
        BufferedImage rescaledImage = getScaledInstance(inputImage, newWidth, newHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
        ImageIO.write(rescaledImage, "jpg", outputFile);
        return rescaledImage;
    }

   /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
    private BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality)   {
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
            BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            } else if (w < targetWidth)  {
                w = targetWidth;
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            } else if (h < targetHeight)  {
                h = targetHeight;
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

}
