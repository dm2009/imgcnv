package org.imgcnv.service;

import java.io.IOException;
import java.nio.file.Path;

import org.imgcnv.exception.PersistentException;
import org.imgcnv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coobird.thumbnailator.Thumbnails;

public class ResizeServiceImageThumbImpl implements ResizeService {
    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public synchronized long createResizedCopy(int scaledWidth, int scaledHeight, String fileName, Path destination) {

        String fullFileName = Utils.getImageName(fileName, destination, Integer.toString(scaledWidth) + "th");
        long result = -1;
        try {
            logger.info("ResizedCopy started: {}", fullFileName);
            long timeout = System.currentTimeMillis();
            Thumbnails.of(fileName)
                    .forceSize(scaledWidth, scaledWidth)
                    .outputQuality(1.0f)
                    .toFile(fullFileName);
            result = 1;
            timeout = System.currentTimeMillis() - timeout;
            logger.info("ResizedCopy: {} end timeout {}", fullFileName, timeout);
        } catch (IOException e) {
            result = -1;
            throw new PersistentException(e);
        }
        return result;
    }

}
