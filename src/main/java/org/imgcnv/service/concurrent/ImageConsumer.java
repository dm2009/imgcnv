package org.imgcnv.service.concurrent;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.imgcnv.exception.ApplicationException;
import org.imgcnv.service.concurrent.resize.ResizeBufferedImageService;
import org.imgcnv.utils.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consumer which consume downloaded images.
 * @author Dmitry_Slepchenkov
 *
 */
public class ImageConsumer implements Runnable {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * JobMapConfig.
     */
    private JobMapConfig jobMap;
    /**
     * ExecutorService for execute tasks.
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    /**
     * Resize service for image convert.
     */
    private ResizeBufferedImageService resizeService;

    /**
     * QueueConfig blockQueue for storage task.
     */
    private QueueConfig itemQueue;

    /**
     *
     * @return ExecutorService.
     */
    public final ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     *
     * @param executorServiceParam
     *            the ExecutorService to set.
     */
    public final void setExecutorService(
            final ExecutorService executorServiceParam) {
        this.executorService = executorServiceParam;
    }

    /**
     *
     * @param itemQueueParam
     *            the QueueConfig to set.
     */
    public final void setItemQueue(final QueueConfig itemQueueParam) {
        this.itemQueue = itemQueueParam;
    }

    /**
     *
     * @return JobMapConfig, which encapsulate JobMap.
     */
    public final JobMapConfig getJobMap() {
        return jobMap;
    }

    /**
     *
     * @param jobMapParam
     *            the JobMapConfig object to set.
     */
    public final void setJobMap(final JobMapConfig jobMapParam) {
        this.jobMap = jobMapParam;
    }

    /**
     *
     * @return ResizeBufferedImageService.
     */
    public final ResizeBufferedImageService getResizeService() {
        return resizeService;
    }

    /**
     *
     * @param resizeServiceParam
     *            the ResizeBufferedImageService to set.
     */
    public final void setResizeService(
            final ResizeBufferedImageService resizeServiceParam) {
        this.resizeService = resizeServiceParam;
    }

    /**
     * method, which prepare image resizing.
     */
    public final void consume() {
        ImageObject imageObject = null;
        BufferedImage image = null;
        try {
            imageObject = itemQueue.getBlockingQueue().take();
            if (imageObject != null) {
                image = imageObject.getFuture().get();
                logger.info("Take image for id {} url {}", imageObject.getId(),
                        imageObject.getResource().getUrl());
            }
        } catch (InterruptedException e) {
            throw new ApplicationException(e);
        } catch (ExecutionException e) {
            throw new ApplicationException(e);
        }

        if (imageObject != null) {

            Long id = imageObject.getId();
            String url = imageObject.getResource().getUrl();
            List<JobFutureObject> tasks = jobMap.getMap().get(id);

            for (JobFutureObject fo : tasks) {
                if (fo.getResource().getUrl().equals(url)) {
                    CopyOnWriteArrayList<Future<Boolean>> futureImages =
                            fo.getFutureImages();
                    List<Integer> thumbails = Arrays.asList(Consts.SIZE_THUMB_1,
                            Consts.SIZE_THUMB_2,
                            Consts.SIZE_THUMB_3);

                    for (Integer thumbail : thumbails) {
                        ConvertImageCallable callable =
                                new ConvertImageCallable();
                        callable.setIndex(id);
                        callable.setUrl(url);
                        callable.setResolution(thumbail);
                        callable.setResizeService(resizeService);
                        callable.setImage(image); // not null?
                        futureImages.add(executorService.submit(callable));
                    }
                }
            }
        }
    }

    @Override
    /**
     * Runnable interface implement.
     */
    public final void run() {
        while (true) {
            consume();
        }

    }

}
