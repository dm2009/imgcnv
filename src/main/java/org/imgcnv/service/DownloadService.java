package org.imgcnv.service;

import java.nio.file.Path;

public interface DownloadService {
    
    long download(String url, Path destination);

}
