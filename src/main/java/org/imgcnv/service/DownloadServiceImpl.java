package org.imgcnv.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.imgcnv.exception.PersistentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.validator.routines.UrlValidator;

public class DownloadServiceImpl implements DownloadService {
    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public synchronized long download(String url, Path destination) {
        long result = -1;
        UrlValidator urlValidator = new UrlValidator();
        URL uri = null;
        if (urlValidator.isValid(url)) {
            try {
                uri = new URL(url);
            } catch (MalformedURLException e) {
                result = -1;
                throw new PersistentException(e);
            }
        }

        if (uri != null) {
            try (InputStream in = uri.openStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
                logger.info("download:  {} -> {}", url, destination);
                result = destination.toFile().length();
            } catch (IOException e) {
                result = -1;
                throw new PersistentException(e);
            }
        }

        return result;
    }

}
