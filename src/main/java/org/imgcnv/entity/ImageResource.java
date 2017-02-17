package org.imgcnv.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.imgcnv.utils.Consts;

/**
 * Class wrapper for url resource. Created by Dmitry_Slepchenkov on 2/1/2017.
 */
public class ImageResource implements Serializable {

    /**
     * UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * url on image file.
     */
    private String url;

    /**
     *
     * @return String url.
     */
    public final String getUrl() {
        return url;
    }

    /**
     *
     * @param urlParam
     *            String to set.
     */
    public final void setUrl(final String urlParam) {
        this.url = urlParam;
    }

    /**
     * Constructor from superclass.
     */
    public ImageResource() {
    }

    /**
     * Constructor with field.
     *
     * @param urlParam
     *            String url on image file.
     */
    public ImageResource(final String urlParam) {
        this.url = urlParam;
    }

    /**
     * Convert string with urls to List<ImageResource>.
     *
     * @param params
     *            String with list of url with delimiters
     * @return List<ImageResource>
     */
    public static List<ImageResource> imageResourceListFromString(
            final String params) {
        List<ImageResource> list = new ArrayList<ImageResource>();
        String[] str = params.split(Consts.DELIMITER);
        for (String url : str) {
            list.add(new ImageResource(url));
        }
        return list;
    }

    /**
     * Convert string with urls to Set<ImageResource>.
     *
     * @param params
     *            String with list of url with delimiters
     * @return Set<ImageResource>
     */
    public static Set<ImageResource> imageResourceSetFromString(
            final String params) {
        Set<ImageResource> set = new HashSet<ImageResource>();
        String[] str = params.split(Consts.DELIMITER);
        for (String url : str) {
            set.add(new ImageResource(url));
        }
        return set;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return new StringBuilder("ImageResource [url=")
                .append(getUrl())
                .append("]")
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (url != null) {
            result += url.hashCode();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ImageResource other = (ImageResource) obj;
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }

}
