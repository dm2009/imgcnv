package org.imgcnv.concurrent;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.imgcnv.service.DownloadService;
import org.imgcnv.utils.Utils;

public class CopyCallable implements Callable<Boolean> {

    private DownloadService downloadService;

    private String url;
    private Long index;

    public DownloadService getDownloadService() {
        return downloadService;
    }

    public void setDownloadService(DownloadService downloadService) {
        this.downloadService = downloadService;
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

    @Override
    public Boolean call() throws Exception {

        String copyPath = new Utils().getCopyPath();
        Utils.createDir(copyPath);

        String fileName = Utils.getFileName(url);
        String targetFolderLink = copyPath + File.separator + index.toString();
        Utils.createDir(targetFolderLink);

        Path targetPath = new File(targetFolderLink + File.separator + fileName).toPath();
        long copyResult = downloadService.download(url, targetPath);

        if (copyResult > 0) {
            JobExecutor executor = JobExecutor.getInstance();
            executor.startConvert(index, url);
        }

        return copyResult > 0;
    }

}
