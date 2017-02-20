package org.imgcnv.service;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import org.imgcnv.exception.PersistentException;
import org.imgcnv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;

/**
 * Service for image scaling. Use net.coobird.thumbnailator.Thumbnails library
 * for image conversion
 * Support sharp filter
 * @author Dmitry_Slepchenkov
 *
 */
public class ResizeServiceImageThumbImpl implements ResizeService {
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

        String fullFileName = Utils.getImageName(fileName, destination,
                Integer.toString(scaledWidth) + "th");
        String newFileExt = Utils.getFileExt(fullFileName);

        long result = -1;
        try {
            logger.info("ResizedCopy started: {}", fullFileName);
            long timeout = System.currentTimeMillis();
            if (!"GIF".equals(newFileExt.toUpperCase())) {
                BufferedImage bi = Utils.sharper(
                        Thumbnails.of(fileName)
                        .outputQuality(1.0f)
                        .antialiasing(Antialiasing.OFF)
                        .size(scaledWidth, scaledWidth)
                        .asBufferedImage(), Utils.OP_SHARP_MIDDLE);

                //ImageIO.write(bi, newFileExt, new File(fullFileName));
                Thumbnails.of(bi)
                .outputQuality(1.0f)
                .antialiasing(Antialiasing.OFF)
                .size(scaledWidth, scaledWidth)
                .toFile(fullFileName);
            } else {
                Thumbnails.of(fileName)
                .outputQuality(1.0f)
                .antialiasing(Antialiasing.OFF)
                .size(scaledWidth, scaledWidth)
                .toFile(fullFileName);
            }

            result = 1;
            timeout = System.currentTimeMillis() - timeout;
            logger.info("ResizedCopy: {} end timeout {}",
                    fullFileName, timeout);
        } catch (Exception e) {
            result = -1;
            throw new PersistentException(e);
        }
        return result;
    }

}
