package org.imgcnv.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.imgcnv.utils.Consts;

/**
 * Created by Dmitry_Slepchenkov on 2/1/2017.
 */
public class ImageResource implements Serializable {

    private static final long serialVersionUID = 1L;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageResource() {
    }

    public ImageResource(String url) {
        this.url = url;
    }

    public static List<ImageResource> ImageResourceListFromString(String params) {
        List<ImageResource> list = new ArrayList<ImageResource>();
        String[] str = params.split(Consts.DELIMITER);
        for (String url : str) {
            list.add(new ImageResource(url));
        }
        return list;
    }

    public static Set<ImageResource> ImageResourceSetFromString(String params) {
        Set<ImageResource> set = new HashSet<ImageResource>();
        String[] str = params.split(Consts.DELIMITER);
        for (String url : str) {
            set.add(new ImageResource(url));
        }
        return set;
    }

    @Override
    public String toString() {
        return new StringBuilder("ImageResource [url=")
                .append(getUrl())
                .append("]").toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImageResource other = (ImageResource) obj;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

}
