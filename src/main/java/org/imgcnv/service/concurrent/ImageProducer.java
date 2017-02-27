package org.imgcnv.service.concurrent;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.imgcnv.entity.ImageResource;
import org.imgcnv.exception.ApplicationException;
import org.imgcnv.service.concurrent.download.DownloadService;
import org.imgcnv.utils.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class produce task for download file.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ImageProducer implements Producer, ImageCallback {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Generator for unique index.
     */
    private IdGenerator idGenerator;

    /**
     * Queue for link producer with consumer.
     */
    private QueueConfig itemQueue;

    /**
     * Map for store jobs.
     */
    private JobMapConfig jobMap;

    /**
     * ExecutorService to start tasks for download.
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(
            Consts.DOWNLOAD_THREADS);

    /**
     * Download service.
     */
    private DownloadService downloadService;

    /**
     *
     * @return IdGenerator
     */
    public final IdGenerator getIdGenerator() {
        return idGenerator;
    }

    /**
     *
     * @param generator
     *            the IdGenerator to set.
     */
    public final void setIdGenerator(final IdGenerator generator) {
        this.idGenerator = generator;
    }

    /**
     *
     * @return QueueConfig.
     */
    public final QueueConfig getItemQueue() {
        return itemQueue;
    }

    /**
     *
     * @param queue
     *            the QueueConfig to set.
     */
    public final void setItemQueue(final QueueConfig queue) {
        this.itemQueue = queue;
    }

    /**
     *
     * @return JobMapConfig.
     */
    public final JobMapConfig getJobMap() {
        return jobMap;
    }

    /**
     *
     * @param jobMapParam
     *            the JobMapConfig to set.
     */
    public final void setJobMap(final JobMapConfig jobMapParam) {
        this.jobMap = jobMapParam;
    }

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
     * @return DownloadService.
     */
    public final DownloadService getDownloadService() {
        return downloadService;
    }

    /**
     *
     * @param downloadServiceArg
     *            the DownloadService to set.
     */
    public final void setDownloadService(
            final DownloadService downloadServiceArg) {
        this.downloadService = downloadServiceArg;
    }

    /**
     * Creates tasks for download files from url list.
     *
     * @param resource List<ImageResource>
     *            resource with urls.
     * @return long job id.
     */
    @Override
    public final long addToProducer(final List<ImageResource> resource) {
        long id = idGenerator.getNextId();

        List<JobFutureObject> tasks =
                new CopyOnWriteArrayList<JobFutureObject>();

        for (ImageResource item : resource) {
            // for map
            JobFutureObject future = new JobFutureObject();
            future.setResource(item);
            future.setDate(new Date());

            // image object
            ImageObject imageObject = new ImageObject();
            imageObject.setResource(item);
            imageObject.setId(id);

            DownloadImageCallable callable = new DownloadImageCallable();
            callable.setDownloadService(downloadService);
            callable.setCallback(this);
            callable.setImageObject(imageObject);

            imageObject.setFuture(executorService.submit(callable));

            // assamble jobMap
            CopyOnWriteArrayList<Future<Boolean>> futureImages =
                    new CopyOnWriteArrayList<Future<Boolean>>();
            future.setFutureImages(futureImages);
            future.setFuture(imageObject.getFuture());
            tasks.add(future);
        }
        jobMap.getMap().put(id, tasks);
        logger.info("Put tasks id={} to map", id);

        return id;
    }

    /**
     * Callback function.
     *
     * @param imageObject ImageObject
     */
    @Override
    public final void callFinished(final ImageObject imageObject) {

        try {
            itemQueue.getBlockingQueue().put(imageObject);
            logger.info("Put imageObject id={} url={} into Queue",
                    imageObject.getId(),
                    imageObject.getResource().getUrl());

        } catch (InterruptedException e) {
            throw new ApplicationException(e);
        }

    }


}
