package org.imgcnv.service.concurrent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.imgcnv.service.concurrent.download.DownloadService;
import org.imgcnv.service.concurrent.download.DownloadServiceImpl;
import org.imgcnv.service.concurrent.read.ReadService;
import org.imgcnv.service.concurrent.read.ReadServiceImpl;
import org.imgcnv.utils.Utils;

/**
 * Class implements callable interface for task to download image.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public final class DownloadImageCallable implements Callable<Boolean> {

    //Required params
    /**
     * ImageObject for store information.
     */
    private ImageObject imageObject;

    /**
     * Callback function for return result.
     */
    private ImageCallback callback;

    //Optional params
    /**
     * Download service for image download.
     */
    private DownloadService downloadService;

    /**
     * Read image service for image download.
     */
    private ReadService readService;

    /**
     * Class for Builder pattern (constructor for DownloadImageCallable).
     *
     * @author Dmitry_Slepchenkov
     *
     */
    public static class Builder {

        /**
         * ImageObject for store information.
         */
        private final ImageObject imageObject;

        /**
         * Callback function for return result.
         */
        private final ImageCallback callback;

        /**
         * Download service for image download.
         */
        private DownloadService downloadService =
                new DownloadServiceImpl();

        /**
         * Read service for image read.
         */
        private ReadService readService =
                new ReadServiceImpl();

        /**
         * Builder constructor with params.
         *
         * @param imageObjectParam
         *            as ImageObject to set.
         * @param callbackParam
         *            as ImageCallback to set.
         */
        public Builder(final ImageObject imageObjectParam,
                final ImageCallback callbackParam) {
            this.imageObject = imageObjectParam;
            this.callback = callbackParam;
        }

        /**
         * Used to set DownloadService.
         *
         * @param downloadServiceParam
         *            as DownloadService
         * @return Builder
         */
        public final Builder downloadService(final DownloadService
                downloadServiceParam) {
            downloadService = downloadServiceParam;
            return this;
        }

        /**
         * build method of Builder pattern.
         *
         * @return DownloadImageCallable.
         */
        public final DownloadImageCallable build() {
            return new DownloadImageCallable(this);
        }
    }

    /**
     * Constructor for this class, for builder.
     *
     * @param builder
     *            to set Builder
     */
    private DownloadImageCallable(final Builder builder) {
        this.imageObject = builder.imageObject;
        this.callback = builder.callback;
        this.downloadService = builder.downloadService;
        this.readService = builder.readService;
    }

    /**
     * @return result of copy image operation. If operation was successful
     *         result is BufferedImage.
     * @throws Exception
     *             if unable to compute a result.
     */
    @Override
    public Boolean call() throws Exception {

        String copyPath = Utils.getCopyPath();
        Utils.createDir(copyPath);
        String url = imageObject.getResource().getUrl();

        String fileName = Utils.getFileName(url);
        String targetFolderLink = copyPath + File.separator
                + imageObject.getId();
        Utils.createDir(targetFolderLink);

        Path targetPath = new File(targetFolderLink + File.separator + fileName)
                .toPath();
        Boolean copyResult = downloadService.download(url, targetPath);
        BufferedImage image = null;

        if (copyResult != null && callback != null) {
            image = readService.read(targetPath);
        }

        if (image != null) {
            //logger.info("image != null, callback != null");
            //Create new object with BufferedImage
            ImageObject modifiedImageObject =
                    new ImageObject.Builder(imageObject.getResource())
                    .jobId(imageObject.getId())
                    .image(image)
                    .build();
            callback.callFinished(modifiedImageObject);
        }

        return image != null;
    }

}
