package org.imgcnv.service.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.imgcnv.utils.Consts;

/**
 * Class hold blockingQueue.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class QueueConfig {

    /**
     * blockingQueue which link producer and consumer.
     */
    private BlockingQueue<ImageObject> blockingQueue =
            new LinkedBlockingQueue<ImageObject>(
            Consts.QUEUE_CAPACITY);

    /**
     *
     * @return BlockingQueue<ImageObject>.
     */
    public final BlockingQueue<ImageObject> getBlockingQueue() {
        return blockingQueue;
    }

    /**
     *
     * @param queue
     *            thr BlockingQueue<ImageObject> to set.
     */
    public final void setBlockingQueue(final BlockingQueue<ImageObject>
    queue) {
        this.blockingQueue = queue;
    }

}
