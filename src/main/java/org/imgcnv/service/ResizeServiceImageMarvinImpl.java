package org.imgcnv.service;

import java.nio.file.Path;

import org.imgcnv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import static marvin.MarvinPluginCollection.*;

public class ResizeServiceImageMarvinImpl implements ResizeService {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public synchronized long createResizedCopy(int scaledWidth, int scaledHeight, String fileName, Path destination) {
        String fullFileName = Utils.getImageName(fileName, destination, Integer.toString(scaledWidth) + "mrv");
        long result = -1;

        logger.info("ResizedCopy: {}", fullFileName);
        long timeout = System.currentTimeMillis();

        MarvinImage image = MarvinImageIO.loadImage(fileName);
        // thresholding(image, 85);
        scale(image.clone(), image, scaledWidth, scaledHeight);
        MarvinImageIO.saveImage(image, fullFileName);
        result = 1;

        timeout = System.currentTimeMillis() - timeout;
        logger.info("ResizedCopy: {} timeout {}", fullFileName, timeout);

        return result;
    }

}
