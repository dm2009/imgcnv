package org.imgcnv.service;

import java.nio.file.Path;

/**
 * Service for image scaling.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public interface ResizeService {
    /**
     * Method create a scaling image.
     *
     * @param scaledWidth
     *            int The width of scaling image
     * @param scaledHeight
     *            int The height of scaling image
     * @param fileName
     *            String Name of input file
     * @param destination
     *            Path Destination for result image
     * @return long Result of operation. If operation is successful return size
     *         of file
     */
    long createResizedCopy(int scaledWidth, int scaledHeight,
            String fileName, Path destination);

}
