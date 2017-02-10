package org.imgcnv.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
//import javax.servlet.ServletContext;
import java.nio.file.Path;

public class Utils {

    public String getPath() {

        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath;
        String reponsePath = "";
        try {
            fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");
            fullPath = pathArr[0];
            reponsePath = new File(fullPath).getPath() + File.separatorChar;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return reponsePath;
    }

    public static void createDir(final String DirPath) {
        File DirPathFolder = new File(DirPath);
        if (!DirPathFolder.exists()) {
            DirPathFolder.mkdir();
        }

    }

    public static String getFileExt(String fileName) {
        String newFileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return newFileExt;
    }

    public static String getImageName(String fileName, Path destination, String index) {
        String newFileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.lastIndexOf("."))
                + index;
        String newFileExt = getFileExt(fileName);
        String fullFileName = destination.toAbsolutePath() + File.separator + newFileName + "." + newFileExt;
        return fullFileName;
    }

    public String getCopyPath() {
        return getPath() + File.separator + "pic";
    }

    public static String getFileName(final String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

}
