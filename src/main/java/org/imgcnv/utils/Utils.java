package org.imgcnv.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
//import javax.servlet.ServletContext;
import java.nio.file.Path;

/**
 * Utility class for application.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class Utils {

    /**
     * Get path to web application directory.
     *
     * @return path to web application directory
     */
    public final String getPath() {

        String path = this.getClass().getClassLoader().getResource("")
                .getPath();
        String fullPath;
        String reponsePath = "";
        try {
            fullPath = URLDecoder.decode(path, "UTF-8");
            String[] pathArr = fullPath.split("/WEB-INF/classes/");
            fullPath = pathArr[0];
            reponsePath = new File(fullPath).getPath() + File.separatorChar;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return reponsePath;
    }

    /**
     * Create directory from string with path.
     *
     * @param dirPath
     *            String Path to directory
     */
    public static final void createDir(final String dirPath) {
        File dirPathFolder = new File(dirPath);
        if (!dirPathFolder.exists()) {
            dirPathFolder.mkdir();
        }

    }

    /**
     * Get file extension.
     *
     * @param fileName
     *            String Name of file
     * @return String File extension
     */
    public static final String getFileExt(final String fileName) {
        String newFileExt = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length());
        return newFileExt;
    }

    /**
     * Construct file name, using index for path.
     *
     * @param fileName
     *            String Name of file
     * @param destination
     *            Path for destination
     * @param index
     *            used for creating additional folder
     * @return String full filename for image.
     */
    public static final String getImageName(final String fileName,
            final Path destination, final String index) {
        String newFileName = fileName.substring(fileName.lastIndexOf(
                File.separator) + 1, fileName.lastIndexOf(".")) + index;
        String newFileExt = getFileExt(fileName);
        String fullFileName = destination.toAbsolutePath() + File.separator
                + newFileName + "." + newFileExt;
        return fullFileName;
    }

    /**
     * Return path to directory on web application with images.
     *
     * @return String path to directory on web application with images
     */
    public final String getCopyPath() {
        return getPath() + File.separator + "pic";
    }

    /**
     * Return filename only from url.
     *
     * @param url
     *            String url with filename
     * @return String filename only
     */
    public static String getFileName(final String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    /**
     * A {@link ConvolveOp} using a very strong "sharp" kernel that acts
     * (softens the image a bit) when applied to an image.
     */
    public static final ConvolveOp OP_SHARP_MORE = new ConvolveOp(
            new Kernel(3, 3, new float[]
                    {-1.f, -1.f, -1.f, -1.f, 9.0f, -1.f, -1.f, -1.f, -1.f}),
            ConvolveOp.EDGE_NO_OP, null);

    /**
     * A {@link ConvolveOp} using a light "sharp" kernel that acts
     * (softens the image a bit) when applied to an image.
     */
    public static final ConvolveOp OP_SHARP_LIGHT = new ConvolveOp(
            new Kernel(3, 3, new float[]
                    {0.f, -1.f, 0.f, -1.f, 5.0f, -1.f, 0.f, -1.f, 0.f}),
            ConvolveOp.EDGE_NO_OP, null);

    /**Sharper example filter.
     *
     * @param originalPic input BufferedImage
     * @return BufferedImage Pic with sharp filter
     */
    public static BufferedImage sharper(final BufferedImage originalPic) {
        int imageWidth = originalPic.getWidth();
        int imageHeight = originalPic.getHeight();

        BufferedImage newPic = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_3BYTE_BGR);

        ConvolveOp co = OP_SHARP_LIGHT;
        co.filter(originalPic, newPic);

        return newPic;
}
}
