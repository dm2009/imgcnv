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

public class JobExecutor implements Callback {

    private static JobExecutor instance = new JobExecutor();

    private AtomicLong index;
    private ConcurrentHashMap<Long, List<FutureObject>> hm;
    private ExecutorService es;

    private DownloadService downloadService;
    private ResizeService resizeService;

    public AtomicLong getIndex() {
        return index;
    }

    public void setIndex(AtomicLong index) {
        this.index = index;
    }

    public ConcurrentHashMap<Long, List<FutureObject>> getHm() {
        return hm;
    }

    public void setHm(ConcurrentHashMap<Long, List<FutureObject>> hm) {
        this.hm = hm;
    }

    public ExecutorService getEs() {
        return es;
    }

    public void setEs(ExecutorService es) {
        this.es = es;
    }

    public DownloadService getDownloadService() {
        return downloadService;
    }

    public void setDownloadService(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    public ResizeService getResizeService() {
        return resizeService;
    }

    public void setResizeService(ResizeService resizeService) {
        this.resizeService = resizeService;
    }

    private JobExecutor() {
        hm = new ConcurrentHashMap<Long, List<FutureObject>>();
        index = new AtomicLong(0);
        es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        downloadService = new DownloadServiceImpl();
        resizeService = new ResizeServiceImageThumbImpl();

    }

    public static JobExecutor getInstance() {
        return instance;
    }

    public long addToExecutor(List<ImageResource> ob) {
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

            CopyOnWriteArrayList<Future<Boolean>> futureImages = new CopyOnWriteArrayList<Future<Boolean>>();
            future.setFutureImages(futureImages);

            future.setFuture(es.submit(callable));

            tasks.add(future);
        }
        hm.put(id, tasks);
        return id;
    }

    private void startConvert(long id, String url) {
        List<FutureObject> tasks = hm.get(id);

        ListIterator<FutureObject> it = tasks.listIterator();
        while (it.hasNext()) {
            FutureObject fo = it.next();
            if (fo.getResource().getUrl().equals(url)) {
                CopyOnWriteArrayList<Future<Boolean>> futureImages = fo.getFutureImages();
                List<Integer> thumbails = Arrays.asList(Consts.SIZE_THUMB_1, Consts.SIZE_THUMB_2, Consts.SIZE_THUMB_3);

                for (Integer index : thumbails) {
                    ConvertCallable callable = new ConvertCallable();
                    callable.setIndex(id);
                    callable.setUrl(url);
                    callable.setResolution(index);
                    callable.setResizeService(resizeService);
                    futureImages.add(es.submit(callable));
                }

                fo.setFutureImages(futureImages);
                it.set(fo);
            }
        }

    }

    @Override
    public void callConvert(long id, String url) {
        startConvert(id, url);
    }

    public boolean isReadyJob(Long id) {
        List<FutureObject> tasks;
        tasks = hm.get(id);

        if (tasks != null) {
            for (FutureObject item : tasks) {
                if (item.getFuture().isDone()) {
                    CopyOnWriteArrayList<Future<Boolean>> list = item.getFutureImages();
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
