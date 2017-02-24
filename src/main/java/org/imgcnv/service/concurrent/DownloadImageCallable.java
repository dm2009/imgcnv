package org.imgcnv.service.concurrent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.imgcnv.service.concurrent.download.DownloadService;
import org.imgcnv.utils.Utils;

/**
 * Class implements callable interface for task to download image.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class DownloadImageCallable implements Callable<BufferedImage> {
    /**
     * Download service for image download.
     */
    private DownloadService downloadService;

    /**
     * Callback function for return result.
     */
    private ImageCallback callback;

    /**
     *
     * @return DownloadService.
     */
    public final DownloadService getDownloadService() {
        return downloadService;
    }

    /**
     * ImageObject for store information.
     */
    private ImageObject imageObject;

    /**
     *
     * @param downloadServiceParam
     *            the downloadService to set.
     */
    public final void setDownloadService(final DownloadService
            downloadServiceParam) {
        this.downloadService = downloadServiceParam;
    }

    /**
     *
     * @return ImageCallback
     */
    public final ImageCallback getCallback() {
        return callback;
    }

    /**
     *
     * @param callbackParam
     *            the callback to set.
     */
    public final void setCallback(final ImageCallback callbackParam) {
        this.callback = callbackParam;
    }

    /**
     *
     * @return ImageObject
     */
    public final ImageObject getImageObject() {
        return imageObject;
    }

    /**
     *
     * @param imageObjectParam
     *            the ImageObject to set.
     */
    public final void setImageObject(final ImageObject imageObjectParam) {
        this.imageObject = imageObjectParam;
    }

    /**
     * @return result of copy image operation. If operation was successful
     *         result is BufferedImage.
     * @throws Exception
     *             if unable to compute a result.
     */
    @Override
    public final BufferedImage call() throws Exception {

        String copyPath = new Utils().getCopyPath();
        Utils.createDir(copyPath);
        String url = imageObject.getResource().getUrl();

        String fileName = Utils.getFileName(url);
        String targetFolderLink = copyPath + File.separator
                + imageObject.getId();
        Utils.createDir(targetFolderLink);

        Path targetPath = new File(targetFolderLink + File.separator
                + fileName).toPath();
        BufferedImage copyResult = downloadService.download(url, targetPath);

        if (copyResult != null && callback != null) {
            // logger.info("image != null, callback != null");
            callback.callFinished(imageObject);
        }

        return copyResult;
    }

}
