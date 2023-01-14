package delayedscheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DelayedJobScheduler {
    private final ScheduledExecutorService scheduler;
    private final AtomicInteger jobCounter;
    private final Map<Integer, ScheduledFuture<?>> jobMap;

    public DelayedJobScheduler() {
        scheduler = Executors.newScheduledThreadPool(4);
        jobCounter = new AtomicInteger();
        jobMap = new ConcurrentHashMap<>();
    }

    public int scheduleJob(String jobName, LocalDateTime dateTime, Runnable job) {
        int jobId = getNextJobId();
        long delay = getDelay(dateTime);
        ScheduledFuture<?> future = scheduleTask(jobName, job, delay);
        addJobToMap(jobId, future);
        return jobId;
    }

    private int getNextJobId() {
        return jobCounter.getAndIncrement();
    }

    private long getDelay(LocalDateTime dateTime) {
        return ChronoUnit.MILLIS.between(LocalDateTime.now(), dateTime);
    }

    private ScheduledFuture<?> scheduleTask(String jobName, Runnable job, long delay) {
        return scheduler.schedule(() -> {
            System.out.println(jobName + " started at " + LocalDateTime.now());
            job.run();
            System.out.println(jobName + " completed at " + LocalDateTime.now());
        }, delay, TimeUnit.MILLISECONDS);
    }

    private void addJobToMap(int jobId, ScheduledFuture<?> future) {
        jobMap.put(jobId, future);
    }

    public boolean cancelJob(int jobId) {
        ScheduledFuture<?> future = jobMap.get(jobId);
        if (future != null) {
            if (future.cancel(false)) {
                System.out.println("Job with id " + jobId + " has been successfully cancelled.");
                return true;
            } else {
                System.out.println("Job with id " + jobId + " could not be cancelled as it has already started running.");
                return false;
            }
        } else {
            System.out.println("No job found with id " + jobId + ".");
            return false;
        }
    }


    public void shutdown() {
        scheduler.shutdown();
    }
}
