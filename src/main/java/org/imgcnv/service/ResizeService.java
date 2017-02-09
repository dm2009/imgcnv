package org.imgcnv.service;

import java.nio.file.Path;

public interface ResizeService {
    
    long createResizedCopy(int scaledWidth, int scaledHeight, String fileName, Path destination);

}
