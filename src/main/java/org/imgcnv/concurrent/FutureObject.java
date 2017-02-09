package org.imgcnv.concurrent;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

public class FutureObject {
    private String url;
    private Future<Boolean> future;
    private CopyOnWriteArrayList<Future<Boolean>> futureImages;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Future<Boolean> getFuture() {
        return future;
    }

    public void setFuture(Future<Boolean> future) {
        this.future = future;
    }

    public CopyOnWriteArrayList<Future<Boolean>> getFutureImages() {
        return futureImages;
    }

    public void setFutureImages(CopyOnWriteArrayList<Future<Boolean>> futureImages) {
        this.futureImages = futureImages;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((future == null) ? 0 : future.hashCode());
        result = prime * result + ((futureImages == null) ? 0 : futureImages.hashCode());
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
        FutureObject other = (FutureObject) obj;
        if (future == null) {
            if (other.future != null)
                return false;
        } else if (!future.equals(other.future))
            return false;
        if (futureImages == null) {
            if (other.futureImages != null)
                return false;
        } else if (!futureImages.equals(other.futureImages))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

}
