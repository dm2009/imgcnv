package org.imgcnv.concurrent;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.imgcnv.service.DownloadService;
import org.imgcnv.utils.Utils;

/**
 * Class implements callable interface for task to download image.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class DownloadCallable implements Callable<Boolean> {

    /**
     * Download service for image download.
     */
    private DownloadService downloadService;

    /**
     * Callback function for return result.
     */
    private Callback callback;

    /**
     * url, associated with image file.
     */
    private String url;

    /**
     * index which define job id (number) and folder to store images.
     */
    private Long index;

    /**
     *
     * @return DownloadService.
     */
    public final DownloadService getDownloadService() {
        return downloadService;
    }

    /**
     *
     * @param downloadServiceParam
     *            the downloadService to set.
     */
    public final void setDownloadService(
            final DownloadService downloadServiceParam) {
        this.downloadService = downloadServiceParam;
    }

    /**
     *
     * @return index.
     */
    public final Long getIndex() {
        return index;
    }

    /**
     *
     * @param indexParam
     *            the index to set.
     */
    public final void setIndex(final Long indexParam) {
        this.index = indexParam;
    }

    /**
     *
     * @return url.
     */
    public final String getUrl() {
        return url;
    }

    /**
     *
     * @param urlParam
     *            the url to set.
     */
    public final void setUrl(final String urlParam) {
        this.url = urlParam;
    }

    /**
     *
     * @return Callback
     */
    public final Callback getCallback() {
        return callback;
    }

    /**
     *
     * @param callbackParam
     *            the callback to set.
     */
    public final void setCallback(final Callback callbackParam) {
        this.callback = callbackParam;
    }

    /**
     * @return result of copy image operation. If operation was successful
     *         result is true.
     * @throws Exception
     *             if unable to compute a result.
     */
    @Override
    public final Boolean call() throws Exception {

        String copyPath = new Utils().getCopyPath();
        Utils.createDir(copyPath);

        String fileName = Utils.getFileName(url);
        String targetFolderLink = copyPath + File.separator + index.toString();
        Utils.createDir(targetFolderLink);

        Path targetPath = new File(targetFolderLink + File.separator + fileName)
                .toPath();
        long copyResult = downloadService.download(url, targetPath);

        if (copyResult > 0 && callback != null) {
            callback.callConvert(index, url);
        }

        return copyResult > 0;
    }

}
