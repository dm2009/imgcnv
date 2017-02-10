package org.imgcnv.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.imgcnv.exception.PersistentException;
import org.imgcnv.utils.Utils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResizeServiceImageImgsrImpl implements ResizeService {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public synchronized long createResizedCopy(int scaledWidth, int scaledHeight, String fileName, Path destination) {

        String fullFileName = Utils.getImageName(fileName, destination, Integer.toString(scaledWidth) + "imgsr");
        String newFileExt = Utils.getFileExt(fullFileName);

        long result = -1;

        File file = new File(fileName);
        if (file.exists()) {
            BufferedImage inputImage;
            try {
                logger.info("ResizedCopy started: {}", fullFileName);
                long timeout = System.currentTimeMillis();

                inputImage = ImageIO.read(file);
                BufferedImage modifiedImage = Scalr.resize(inputImage, Mode.AUTOMATIC, scaledWidth, scaledHeight);
                ImageIO.write(modifiedImage, newFileExt, new File(fullFileName));
                result = 1;

                timeout = System.currentTimeMillis() - timeout;
                logger.info("ResizedCopy: {} end timeout {}", fullFileName, timeout);
            } catch (IOException e) {
                result = -1;
                throw new PersistentException(e);
            }
        }
        return result;
    }

}
