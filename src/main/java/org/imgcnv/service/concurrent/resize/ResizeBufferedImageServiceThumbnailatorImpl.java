package org.imgcnv.service.concurrent.resize;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.imgcnv.exception.ApplicationException;
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
public class ResizeBufferedImageServiceThumbnailatorImpl implements
ResizeBufferedImageService {
    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public final long createResizedCopy(final int scaledWidth,
            final int scaledHeight,
            final BufferedImage bufferedImage,
            final String url,
            final Path destination) {

        String fullFileName = Utils.getImageName(Utils.getFileName(url),
                destination, Integer.toString(scaledWidth) + "th");
        String newFileExt = Utils.getFileExt(fullFileName);

        long result = -1;

        if (bufferedImage == null) {
            return result;
        }

        try {
            //logger.info("ResizedCopy started: {}", fullFileName);
            long timeout = Instant.now().getNano();
            if (!"GIF".equals(newFileExt.toUpperCase())) {
                BufferedImage bi = null;
                synchronized (bufferedImage) {
                    bi = Utils.sharper(
                            Thumbnails.of(bufferedImage)
                            .outputQuality(1.0f)
                            .antialiasing(Antialiasing.OFF)
                            .size(scaledWidth, scaledWidth)
                            .asBufferedImage(), Utils.OP_SHARP_MIDDLE);

                }
                //ImageIO.write(bi, newFileExt, new File(fullFileName));
                Thumbnails.of(bi)
                .outputQuality(1.0f)
                .antialiasing(Antialiasing.OFF)
                .size(scaledWidth, scaledWidth)
                .toFile(fullFileName);
            } else {
                synchronized (bufferedImage) {
                Thumbnails.of(bufferedImage)
                .outputQuality(1.0f)
                .antialiasing(Antialiasing.OFF)
                .size(scaledWidth, scaledWidth)
                .toFile(fullFileName); //GIF has not a good quality. :(
                }
            }
            result = 1;
            timeout = Instant.now().getNano() - timeout;
            logger.info("ResizedCopy: {} end timeout {} ms",
                 fullFileName,
                 TimeUnit.MILLISECONDS.convert(timeout, TimeUnit.NANOSECONDS));
        } catch (Exception e) {
            result = -1;
            throw new ApplicationException(e);
        }
        return result;
    }

}
