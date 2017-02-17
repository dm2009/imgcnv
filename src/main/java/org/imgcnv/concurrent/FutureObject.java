package org.imgcnv.concurrent;

import java.util.concurrent.CopyOnWriteArrayList;

import java.util.concurrent.Future;

import org.imgcnv.entity.ImageResource;

/**
 * Class for hold Futures objects.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class FutureObject {

    /**
     * ImageResource url wrapper.
     */
    private ImageResource resource;

    /**
     * Future<Boolean> holds image for copy.
     */
    private Future<Boolean> future;

    /**
     * List Future<Boolean> holds images for convert (resize).
     */
    private CopyOnWriteArrayList<Future<Boolean>> futureImages;

    /**
     *
     * @return ImageResource.
     */
    public final ImageResource getResource() {
        return resource;
    }

    /**
     *
     * @param resourceParam
     *            the ImageResource to set.
     */
    public final void setResource(final ImageResource resourceParam) {
        this.resource = resourceParam;
    }

    /**
     *
     * @return Future<Boolean>.
     */
    public final Future<Boolean> getFuture() {
        return future;
    }

    /**
     *
     * @param futureParam
     *            The Future<Boolean> to set.
     */
    public final void setFuture(final Future<Boolean> futureParam) {
        this.future = futureParam;
    }

    /**
     *
     * @return List Future<Boolean>.
     */
    public final CopyOnWriteArrayList<Future<Boolean>> getFutureImages() {
        return futureImages;
    }

    /**
     *
     * @param futureImagesParam
     *            List Future<Boolean> to set.
     */
    public final void setFutureImages(
            final CopyOnWriteArrayList<Future<Boolean>> futureImagesParam) {
        this.futureImages = futureImagesParam;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (future != null) {
            result += future.hashCode();
        }
        if (futureImages != null) {
            result += futureImages.hashCode();
        }
        if (resource != null) {
            result += resource.hashCode();
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
        FutureObject other = (FutureObject) obj;
        if (future == null) {
            if (other.future != null) {
                return false;
            }
        } else if (!future.equals(other.future)) {
            return false;
        }
        if (futureImages == null) {
            if (other.futureImages != null) {
                return false;
            }
        } else if (!futureImages.equals(other.futureImages)) {
            return false;
        }
        if (resource == null) {
            if (other.resource != null) {
                return false;
            }
        } else if (!resource.equals(other.resource)) {
            return false;
        }
        return true;
    }

}
