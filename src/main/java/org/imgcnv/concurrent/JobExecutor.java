package org.imgcnv.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.imgcnv.entity.ImageResource;
import org.imgcnv.service.DownloadService;
import org.imgcnv.service.DownloadServiceImpl;
import org.imgcnv.service.ResizeService;
import org.imgcnv.service.ResizeServiceImageThumbImpl;
import org.imgcnv.utils.Consts;

/**
 * Class organize multithreading process in application.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public final class JobExecutor implements Callback {

    /**
     * Singleton instance.
     */
    private static JobExecutor instance = new JobExecutor();

    /**
     * Unique id of job.
     */
    private AtomicLong index;
    /**
     * Used for store job and List<FutureObject>>.
     */
    private ConcurrentHashMap<Long, List<FutureObject>> hm;
    /**
     * ExecutorService for execute tasks.
     */
    private ExecutorService es;

    /**
     * Download service for image download.
     */
    private DownloadService downloadService;

    /**
     * Resize service for image convert.
     */
    private ResizeService resizeService;

    /**
     *
     * @return index
     */
    public AtomicLong getIndex() {
        return index;
    }

    /**
     *
     * @param indexParam
     *            the index to set.
     */
    public void setIndex(final AtomicLong indexParam) {
        this.index = indexParam;
    }

    /**
     *
     * @return Map<Long, List<FutureObject>>.
     */
    public ConcurrentHashMap<Long, List<FutureObject>> getHm() {
        return hm;
    }

    /**
     *
     * @param hmParam
     *            the Map<Long, List<FutureObject>> to set.
     */
    public void setHm(final ConcurrentHashMap<Long,
            List<FutureObject>> hmParam) {
        this.hm = hmParam;
    }

    /**
     *
     * @return ExecutorService.
     */
    public ExecutorService getEs() {
        return es;
    }

    /**
     *
     * @param esParam
     *            the ExecutorService to set.
     */
    public void setEs(final ExecutorService esParam) {
        this.es = esParam;
    }

    /**
     *
     * @return DownloadService.
     */
    public DownloadService getDownloadService() {
        return downloadService;
    }

    /**
     *
     * @param downloadServiceParam
     *            the DownloadService to set.
     */
    public void setDownloadService(final DownloadService downloadServiceParam) {
        this.downloadService = downloadServiceParam;
    }

    /**
     *
     * @return ResizeService.
     */
    public ResizeService getResizeService() {
        return resizeService;
    }

    /**
     *
     * @param resizeServiceParam
     *            the ResizeService to set.
     */
    public void setResizeService(final ResizeService resizeServiceParam) {
        this.resizeService = resizeServiceParam;
    }

    /**
     * Constructor for this class.
     */
    private JobExecutor() {
        hm = new ConcurrentHashMap<Long, List<FutureObject>>();
        index = new AtomicLong(0);
        es = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors());

        downloadService = new DownloadServiceImpl();
        resizeService = new ResizeServiceImageThumbImpl();
        //resizeService = new ResizeServiceImageImgsrImpl();

    }

    /**
     *
     * @return JobExecutor singleton instance.
     */
    public static JobExecutor getInstance() {
        return instance;
    }

    /**
     * Creates tasks for download files from url list.
     *
     * @param ob
     *            List<ImageResource> with urls.
     * @return id of job.
     */
    public long addToExecutor(final List<ImageResource> ob) {
        long id;
        id = index.incrementAndGet();
        List<FutureObject> tasks = new CopyOnWriteArrayList<FutureObject>();

        for (ImageResource item : ob) {
            FutureObject future = new FutureObject();

            future.setResource(item);
            DownloadCallable callable = new DownloadCallable();
            callable.setIndex(id);
            callable.setUrl(item.getUrl());
            callable.setDownloadService(downloadService);
            callable.setCallback(getInstance());

            CopyOnWriteArrayList<Future<Boolean>> futureImages =
                    new CopyOnWriteArrayList<Future<Boolean>>();
            future.setFutureImages(futureImages);

            future.setFuture(es.submit(callable));

            tasks.add(future);
        }
        hm.put(id, tasks);
        return id;
    }

    /**
     * Create tasks for image convert.
     *
     * @param id
     *            job id
     * @param url
     *            url of image
     */
    private void startConvert(final long id, final String url) {
        List<FutureObject> tasks = hm.get(id);

        ListIterator<FutureObject> it = tasks.listIterator();
        while (it.hasNext()) {
            FutureObject fo = it.next();
            if (fo.getResource().getUrl().equals(url)) {
                CopyOnWriteArrayList<Future<Boolean>> futureImages =
                        fo.getFutureImages();
                List<Integer> thumbails = Arrays.asList(Consts.SIZE_THUMB_1,
                        Consts.SIZE_THUMB_2, Consts.SIZE_THUMB_3);

                for (Integer thumbail : thumbails) {
                    ConvertCallable callable = new ConvertCallable();
                    callable.setIndex(id);
                    callable.setUrl(url);
                    callable.setResolution(thumbail);
                    callable.setResizeService(resizeService);
                    futureImages.add(es.submit(callable));
                }

                fo.setFutureImages(futureImages);
                it.set(fo);
            }
        }

    }

    /**
     * Callback function.
     *
     * @param id
     *            job id
     * @param url
     *            url of image
     */
    @Override
    public void callConvert(final long id, final String url) {
        startConvert(id, url);
    }

    /**
     * Return status of job.
     *
     * @param id
     *            job id
     * @return true when job is ready.
     */
    public boolean isReadyJob(final Long id) {
        List<FutureObject> tasks;
        tasks = hm.get(id);

        if (tasks != null) {
            for (FutureObject item : tasks) {
                if (item.getFuture().isDone()) {
                    CopyOnWriteArrayList<Future<Boolean>> list =
                            item.getFutureImages();
                    if (list.size() == 0) {
                        return false;
                    } else {
                        for (Future<Boolean> child : list) {
                            if (!child.isDone()) {
                                return false;
                            }
                        }
                    }
                } else {
                    return false;
                }
            }

        } else {
            return false;
        }

        return true;
    }

}
