package org.imgcnv.service;

import java.nio.file.Path;

import org.imgcnv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import static marvin.MarvinPluginCollection.scale;

/**
 * Service for image scaling. Use Marvin library for image conversion
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ResizeServiceImageMarvinImpl implements ResizeService {

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
        String fullFileName = Utils.getImageName(
                fileName, destination, Integer.toString(scaledWidth) + "mrv");
        long result = -1;
        logger.info("ResizedCopy started: {}", fullFileName);
        long timeout = System.currentTimeMillis();
        MarvinImage image = MarvinImageIO.loadImage(fileName);
        scale(image.clone(), image, scaledWidth, scaledHeight);
        MarvinImageIO.saveImage(image, fullFileName);
        result = 1;
        timeout = System.currentTimeMillis() - timeout;
        logger.info("ResizedCopy: {} end timeout {}", fullFileName, timeout);
        return result;
    }

}
