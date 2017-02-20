package org.imgcnv.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgcnv.exception.PersistentException;
import org.imgcnv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for image scaling. Use java.awt.Graphics2D for image conversion
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ResizeServiceImageIOImpl implements ResizeService {
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

        Map<String, Boolean> fileType = new HashMap<String, Boolean>();
        fileType.put("BMP", false);
        fileType.put("GIF", false);
        fileType.put("PNG", false);
        fileType.put("JPG", true);
        fileType.put("JPEG", true);

        String fullFileName = Utils.getImageName(fileName, destination,
                Integer.toString(scaledWidth));
        long result = -1;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                long timeout = System.currentTimeMillis();
                BufferedImage originalImage = ImageIO.read(file);

                int realWidth = originalImage.getWidth();
                int realHeight = originalImage.getHeight();
                int maxSize = Math.max(realWidth, realHeight);
                int newWidth = scaledWidth;
                int newHeight = scaledHeight;

                if (realWidth <= 0 || realHeight <= 0 || scaledWidth <= 0
                        || scaledHeight <= 0) {
                    return -1;
                }

                if (scaledWidth <= maxSize) {
                    float koef = 1.0f * maxSize / scaledWidth;
                    newWidth = Math.round(realWidth / koef);
                    newHeight = Math.round(realHeight / koef);
                } else {
                    float koef = 1.0f * scaledWidth / maxSize;
                    newWidth = Math.round(realWidth / koef);
                    newHeight = Math.round(realHeight / koef);
                }

                BufferedImage scaled = new BufferedImage(newWidth, newHeight,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = scaled.createGraphics();

                // using bilinear interpolation
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_OFF);

                g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                g.dispose();

                scaled = Utils.sharper(scaled, Utils.OP_SHARP_LIGHT);

                result = Utils.saveBufferedImage(scaled, fullFileName);
                timeout = System.currentTimeMillis() - timeout;
                logger.info("ResizedCopy:{} end timeout{}", fullFileName,
                        timeout);
            } else {
                result = -1;
                logger.info("Usupported file extension: {}", fullFileName);
            }
        } catch (Exception e) {
            result = -1;
            throw new PersistentException(e);
        }
        return result;
    }

}
