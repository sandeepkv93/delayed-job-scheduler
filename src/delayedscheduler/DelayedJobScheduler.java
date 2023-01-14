package delayedscheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DelayedJobScheduler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final AtomicInteger jobCounter = new AtomicInteger();
    private final Map<Integer, ScheduledFuture<?>> jobMap = new ConcurrentHashMap<>();

    public int scheduleJob(String jobName, LocalDateTime dateTime, Runnable job) {
        int jobId = jobCounter.getAndIncrement();
        long delay = ChronoUnit.MILLIS.between(LocalDateTime.now(), dateTime);
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            System.out.println(jobName + " started at " + LocalDateTime.now());
            job.run();
            System.out.println(jobName + " completed at " + LocalDateTime.now());
        }, delay, TimeUnit.MILLISECONDS);
        jobMap.put(jobId, future);
        return jobId;
    }

    public boolean cancelJob(int jobId) {
        ScheduledFuture<?> future = jobMap.get(jobId);
        if (future != null) {
            return future.cancel(false);
        }
        return false;
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
