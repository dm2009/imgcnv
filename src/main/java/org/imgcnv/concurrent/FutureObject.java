package org.imgcnv.concurrent;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import org.imgcnv.entity.ImageResource;

public class FutureObject {
    private ImageResource resource;

    private Future<Boolean> future;
    private CopyOnWriteArrayList<Future<Boolean>> futureImages;

    public ImageResource getResource() {
        return resource;
    }

    public void setResource(ImageResource resource) {
        this.resource = resource;
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
        result = prime * result + ((resource == null) ? 0 : resource.hashCode());
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
        if (resource == null) {
            if (other.resource != null)
                return false;
        } else if (!resource.equals(other.resource))
            return false;
        return true;
    }

}
