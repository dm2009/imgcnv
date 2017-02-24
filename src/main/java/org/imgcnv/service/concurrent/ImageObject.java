package org.imgcnv.service.concurrent;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import org.imgcnv.entity.ImageResource;

/**
 * Class store object for Queue.
 * @author Dmitry_Slepchenkov
 *
 */
public class ImageObject {

    /**
     * id for store job number.
     */
    private long id;

    /**
     * ImageResource for store url.
     */
    private ImageResource resource;

    /**
     * Future object for work with Callable.
     */
    private Future<BufferedImage> future;

    /**
     * @return id of job.
     */
    public final long getId() {
        return id;
    }

    /**
     * @param idParam
     *            long to set.
     */
    public final void setId(final long idParam) {
        this.id = idParam;
    }

    /**
     * @return ImageResource.
     */
    public final ImageResource getResource() {
        return resource;
    }

    /**
     * @param resourceParam
     *            the ImageResource to set.
     */
    public final void setResource(final ImageResource resourceParam) {
        this.resource = resourceParam;
    }

    /**
     * @return Future<BufferedImage>.
     */
    public final Future<BufferedImage> getFuture() {
        return future;
    }

    /**
     * @param futureParam
     *            the Future<BufferedImage> to set.
     */
    public final void setFuture(final Future<BufferedImage> futureParam) {
        this.future = futureParam;
    }

}
