package org.imgcnv.service;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.imgcnv.exception.PersistentException;
import org.imgcnv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mortennobel.imagescaling.MultiStepRescaleOp;




/**
 * Service for image scaling. Use org.imgcnv library for image conversion
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ResizeServiceImageImgmnImpl implements ResizeService {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public final synchronized long createResizedCopy(final int scaledWidth,
            final int scaledHeight,
            final String fileName, final Path destination) {
        String fullFileName = Utils.getImageName(fileName, destination,
                Integer.toString(scaledWidth) + "imgsc");
        String newFileExt = Utils.getFileExt(fullFileName);

        long result = -1;

        File file = new File(fileName);
        if (file.exists()) {
            BufferedImage inputImage;
            try {
                logger.info("ResizedCopy start: {}", fullFileName);
                long timeout = System.currentTimeMillis();

                inputImage = ImageIO.read(file);
                /*BufferedImage modifiedImage = new ResampleOp(scaledWidth,
                        scaledHeight).filter(inputImage, null);
                */

                BufferedImage modifiedImage = new MultiStepRescaleOp(
                        scaledWidth, scaledHeight,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR)
                        .filter(inputImage, null);

                ImageIO.write(modifiedImage, newFileExt,
                        new File(fullFileName));
                result = 1;
                timeout = System.currentTimeMillis() - timeout;
                logger.info("ResizedCopy:{} end timeout:{}", fullFileName,
                        timeout);

            } catch (IOException e) {
                result = -1;
                throw new PersistentException(e);
            }
        }

        return result;
    }


}
