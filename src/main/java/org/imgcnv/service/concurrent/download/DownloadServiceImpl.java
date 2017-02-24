package org.imgcnv.service.concurrent.download;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import org.imgcnv.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.validator.routines.UrlValidator;

/**Service implementation for download images from Internet.
*
* @author Dmitry_Slepchenkov
*
*/
public class DownloadServiceImpl implements DownloadService {
    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public final BufferedImage download(
            final String url, final Path destination) {
        BufferedImage image = null;
        UrlValidator urlValidator = new UrlValidator();
        URL uri = null;
        if (urlValidator.isValid(url)) {
            try {
                uri = new URL(url);
            } catch (MalformedURLException e) {
                throw new ApplicationException(e);
            }
        }


        if (uri != null) {
            try (InputStream in = uri.openStream()) {


                Files.copy(in, destination,
                        StandardCopyOption.REPLACE_EXISTING);

                /* clone stream ?
                if (in != null) {
                    image = ImageIO.read(Utils.duplicate(in));
                }*/

                //read local file
                File file = new File(destination.toString());
                if (file.exists()) {
                    image = ImageIO.read(file);
                }

                logger.info("download: {} -> {}, image.isNull: {}", url,
                        destination, String.valueOf(image == null));
            } catch (IOException e) {
                throw new ApplicationException(e);
            }

        }

        return image;
    }
}
