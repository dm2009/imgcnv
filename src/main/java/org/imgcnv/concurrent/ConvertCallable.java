package org.imgcnv.concurrent;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.imgcnv.service.ResizeService;
import org.imgcnv.utils.Utils;

/**
 * Class implements callable interface for task to convert image.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ConvertCallable implements Callable<Boolean> {

    /**
     * Resize service for image convert.
     */
    private ResizeService resizeService;

    /**
     * url, associated with image file.
     */
    private String url;
    /**
     * index which define job id (number) and folder to store images.
     */
    private Long index;
    /**
     * Image resolution in pixels.
     */
    private Integer resolution;

    /**
     *
     * @return ResizeService.
     */
    public final ResizeService getResizeService() {
        return resizeService;
    }

    /**
     *
     * @param resizeServiceParam
     *            the resizeService to set.
     */
    public final void setResizeService(final ResizeService resizeServiceParam) {
        this.resizeService = resizeServiceParam;
    }

    /**
     *
     * @return index
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
     * @return url
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
     * @return resolution
     */
    public final Integer getResolution() {
        return resolution;
    }

    /**
     *
     * @param resolutionParam
     *            the resolution to set.
     */
    public final void setResolution(final Integer resolutionParam) {
        this.resolution = resolutionParam;
    }

    /**
     * @return result of resize image operation. If operation was successful
     *         result is true.
     * @throws Exception
     *             if unable to compute a result
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

        String convPathLink = targetFolderLink + File.separator + "thmb";
        Utils.createDir(convPathLink);

        Path convPath = new File(convPathLink).toPath();

        long resizeResult = resizeService.createResizedCopy(resolution,
                resolution,
                targetPath.toAbsolutePath().toString(), convPath);

        return resizeResult > 0;
    }

}
