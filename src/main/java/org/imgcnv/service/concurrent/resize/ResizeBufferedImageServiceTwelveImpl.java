package org.imgcnv.service.concurrent.resize;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.imgcnv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twelvemonkeys.image.ResampleOp;

/**
 * Service for image scaling. Use com.twelvemonkeys.image.ResampleOp
 *  for image conversion.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ResizeBufferedImageServiceTwelveImpl implements
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

        //ImageIO.scanForPlugins();
        String fullFileName = Utils.getImageName(Utils.getFileName(url),
                destination,
                Integer.toString(scaledWidth) + "tm");
        long result = -1;

        if (bufferedImage == null) {
            return result;
        }

        long timeout = Instant.now().getNano();

        int realWidth = bufferedImage.getWidth();
        int realHeight = bufferedImage.getHeight();
        int maxSize = Math.max(realWidth, realHeight);
        int newWidth = scaledWidth;
        int newHeight = scaledHeight;
        float koef = 1.0f;

        if (realWidth <= 0 || realHeight <= 0 || scaledWidth <= 0
                || scaledHeight <= 0) {
            return result;
        }

        if (scaledWidth <= maxSize) {
            koef = 1.0f * maxSize / scaledWidth;
            newWidth = Math.round(realWidth / koef);
            newHeight = Math.round(realHeight / koef);
        } else {
            koef = 1.0f * scaledWidth / maxSize;
            newWidth = Math.round(realWidth / koef);
            newHeight = Math.round(realHeight / koef);
        }

        BufferedImageOp resampler = new ResampleOp(newWidth, newHeight,
                ResampleOp.FILTER_LANCZOS);
        BufferedImage scaled = null;
        synchronized (bufferedImage) {
        scaled = resampler.filter(bufferedImage, null);
        }

        scaled = Utils.sharper(scaled, Utils.OP_SHARP_MIDDLE);

        result = Utils.saveBufferedImage(scaled, fullFileName);
        timeout = Instant.now().getNano() - timeout;
        logger.info("ResizedCopy:{} end timeout {} ms", fullFileName,
                TimeUnit.MILLISECONDS.convert(timeout, TimeUnit.NANOSECONDS));

        return result;
    }

}
