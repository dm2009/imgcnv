package org.imgcnv.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgcnv.exception.PersistentException;
import org.imgcnv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResizeServiceImageIOImpl implements ResizeService {
    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public synchronized long createResizedCopy(int scaledWidth, int scaledHeight, String fileName, Path destination) {

        Map<String, Boolean> fileType = new HashMap<String, Boolean>();
        fileType.put("BMP", true);
        fileType.put("GIF", true);
        fileType.put("PNG", true);
        fileType.put("JPG", true);
        fileType.put("JPEG", true);

        BufferedImage scaled = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        long result = -1;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                BufferedImage originalImage = ImageIO.read(file);
                g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
                g.dispose();
                String fullFileName = Utils.getImageName(fileName, destination, Integer.toString(scaledWidth));
                String newFileExt = Utils.getFileExt(fullFileName);
                if (fileType.get(newFileExt.toUpperCase()) != null) {
                    logger.info("ResizedCopy started: {}", fullFileName);
                    long timeout = System.currentTimeMillis();
                    ImageIO.write(scaled, newFileExt, new File(fullFileName));
                    result = 1;
                    timeout = System.currentTimeMillis() - timeout;
                    logger.info("ResizedCopy:{} end timeout{}", fullFileName, timeout);
                } else {
                    result = -1;
                    logger.info("Usupported file extension: {}", fullFileName);
                }
            }
        } catch (IOException e) {
            result = -1;
            throw new PersistentException(e);
        }
        return result;
    }

}
