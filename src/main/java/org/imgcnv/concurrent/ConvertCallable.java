package org.imgcnv.concurrent;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.imgcnv.service.ResizeService;
import org.imgcnv.utils.Utils;

public class ConvertCallable implements Callable<Boolean> {

    private ResizeService resizeService;

    private String url;
    private Long index;
    private Integer resolution;

    public ResizeService getResizeService() {
        return resizeService;
    }

    public void setResizeService(ResizeService resizeService) {
        this.resizeService = resizeService;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    @Override
    public Boolean call() throws Exception {

        String copyPath = new Utils().getCopyPath();
        Utils.createDir(copyPath);

        String fileName = Utils.getFileName(url);
        String targetFolderLink = copyPath + File.separator + index.toString();
        Utils.createDir(targetFolderLink);

        Path targetPath = new File(targetFolderLink + File.separator + fileName).toPath();

        String convPathLink = targetFolderLink + File.separator + "thmb";
        Utils.createDir(convPathLink);

        Path convPath = new File(convPathLink).toPath();

        long resizeResult = resizeService.createResizedCopy(resolution, resolution,
                targetPath.toAbsolutePath().toString(), convPath);

        return resizeResult > 0;
    }

}
