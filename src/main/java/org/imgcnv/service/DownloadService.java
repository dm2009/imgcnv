package org.imgcnv.service;

import java.nio.file.Path;

/**Service for download images from Internet.
*
* @author Dmitry_Slepchenkov
*
*/
public interface DownloadService {

    /**Method for download image.
     *
     * @param url String with address of image
     * @param destination Path for save image
     * @return long value, must be more then 0.
     */
    long download(String url, Path destination);

}
