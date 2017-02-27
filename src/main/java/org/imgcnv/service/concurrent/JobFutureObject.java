package org.imgcnv.service.concurrent;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.concurrent.Future;

import org.imgcnv.entity.ImageResource;

/**
 * Class for hold Futures objects.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class JobFutureObject {

    /**
     * ImageResource url wrapper.
     */
    private ImageResource resource;

    /**
     * Future<BufferedImage> holds image for copy.
     */
    private Future<BufferedImage> future;

    /**
     * List Future<Boolean> holds images for convert (resize).
     */
    private CopyOnWriteArrayList<Future<Boolean>> futureImages;

    /**
     * Date hold date for clean up operations.
     */
    private Date date;
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
     * @return Future<BufferedImage>.
     */
    public final Future<BufferedImage> getFuture() {
        return future;
    }

    /**
     *
     * @param futureParam
     *            The Future<BufferedImage> to set.
     */
    public final void setFuture(final Future<BufferedImage> futureParam) {
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
    *
    * @return date Date for clean up.
    */
    public final Date getDate() {
        return date;
    }

    /**
     *
     * @param dateParam the Date to set.
     */
    public final void setDate(final Date dateParam) {
        this.date = dateParam;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (date != null) {
            result += date.hashCode();
        }
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
        JobFutureObject other = (JobFutureObject) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
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
