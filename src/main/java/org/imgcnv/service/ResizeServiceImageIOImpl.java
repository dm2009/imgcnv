package org.imgcnv.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

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

        BufferedImage scaled = new BufferedImage(scaledWidth, scaledHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        long result = -1;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                BufferedImage originalImage = ImageIO.read(file);

                //using bilinear interpolation
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g.drawImage(originalImage, 0, 0, scaledWidth,
                        scaledHeight, null);
                g.dispose();
                String fullFileName = Utils.getImageName(fileName,
                        destination, Integer.toString(scaledWidth));
                String newFileExt = Utils.getFileExt(fullFileName);
                Boolean isJpeg = fileType.get(newFileExt.toUpperCase());
                if (isJpeg != null) {
                    logger.info("ResizedCopy started: {}", fullFileName);
                    long timeout = System.currentTimeMillis();
                    if (isJpeg) {
                        File outPutImage = new File(fullFileName);
                        ImageOutputStream  ios =
                                ImageIO.createImageOutputStream(outPutImage);
                        ImageWriter writer = ImageIO
                                .getImageWritersByFormatName("jpeg").next();
                        ImageWriteParam param = writer.getDefaultWriteParam();
                        param.setCompressionMode(
                                ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(1.0F);
                        writer.setOutput(ios);
                        writer.write(
                                null, new IIOImage(scaled, null, null), param);
                        writer.dispose();
                        ImageIO.write(scaled, newFileExt, ios);
                        ios.close();
                    } else {
                        ImageIO.write(
                                scaled, newFileExt, new File(fullFileName));
                    }
                    result = 1;
                    timeout = System.currentTimeMillis() - timeout;
                    logger.info("ResizedCopy:{} end timeout{}", fullFileName,
                            timeout);
                } else {
                    result = -1;
                    logger.info("Usupported file extension: {}", fullFileName);
                }
            }
        } catch (Exception e) {
            result = -1;
            throw new PersistentException(e);
        }
        return result;
    }

}
