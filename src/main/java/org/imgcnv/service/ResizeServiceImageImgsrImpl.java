package org.imgcnv.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.imgcnv.exception.PersistentException;
import org.imgcnv.utils.Utils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for image scaling. Use org.imgscalr library for image conversion
 * Support sharp filter
 * @author Dmitry_Slepchenkov
 */
public class ResizeServiceImageImgsrImpl implements ResizeService {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public final synchronized long createResizedCopy(
            final int scaledWidth, final int scaledHeight,
            final String fileName, final Path destination) {

        String fullFileName = Utils.getImageName(
                fileName, destination, Integer.toString(scaledWidth) + "imgsr");
        String newFileExt = Utils.getFileExt(fullFileName);

        long result = -1;

        File file = new File(fileName);
        if (file.exists()) {
            BufferedImage inputImage;
            try {
                logger.info("ResizedCopy started: {}", fullFileName);
                long timeout = System.currentTimeMillis();

                inputImage = ImageIO.read(file);

                BufferedImage modifiedImage = Scalr.resize(inputImage,
                        Method.ULTRA_QUALITY, scaledWidth, scaledHeight,
                        Utils.OP_SHARP_LIGHT);

                ImageIO.write(modifiedImage, newFileExt,
                        new File(fullFileName));
                result = 1;

                timeout = System.currentTimeMillis() - timeout;
                logger.info("ResizedCopy: {} end timeout {}", fullFileName,
                        timeout);
            } catch (IOException e) {
                result = -1;
                throw new PersistentException(e);
            }
        }
        return result;
    }

}
