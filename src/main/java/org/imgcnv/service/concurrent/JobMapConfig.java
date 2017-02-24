package org.imgcnv.service.concurrent;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

/**
 * Class holds JobMap for store id of job.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class JobMapConfig {
    /**
     * Map for store Job.
     */
    private ConcurrentHashMap<Long, List<JobFutureObject>> map =
            new ConcurrentHashMap<Long, List<JobFutureObject>>();

    /**
     * Getter for this field.
     *
     * @return map.
     */
    public final ConcurrentHashMap<Long, List<JobFutureObject>> getMap() {
        return map;
    }

    /**
     *
     * @param mapParam
     *            the ConcurrentHashMap to set.
     */
    public final void setMap(final ConcurrentHashMap<Long,
            List<JobFutureObject>> mapParam) {
        this.map = mapParam;
    }

    /**
     * Return status of job.
     *
     * @param id
     *            job id
     * @return true when job is ready.
     */
    public final boolean isReadyJob(final Long id) {
        List<JobFutureObject> tasks;
        tasks = map.get(id);
        if (tasks == null) {
            return false;
            // logger.info("isReadyJob: tasks == null for {}", id);
        }

        for (JobFutureObject item : tasks) {
            if (!item.getFuture().isDone()) {
                return false;
            }

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
        }

        return true;

    }

}
