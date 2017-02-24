package org.imgcnv.service.concurrent;

import java.util.List;

import org.imgcnv.entity.ImageResource;

/**
 * Interface for Producer.
 * @author Dmitry_Slepchenkov
 *
 */
public interface Producer {

    /**
     * Creates tasks for download files from url list.
     *
     * @param resource List<ImageResource>
     *            resource with urls.
     * @return long job id.
     */
    long addToProducer(final List<ImageResource> resource);

}
