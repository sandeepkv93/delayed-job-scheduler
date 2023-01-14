package delayedscheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The DelayedJobScheduler class is responsible for scheduling jobs with a specific delay.
 * It uses the ScheduledExecutor class to schedule the jobs and uses a ConcurrentHashMap to store the scheduled jobs.
 * It also uses an AtomicInteger to generate unique job IDs for each scheduled job.
 */
public class DelayedJobScheduler {
    private final ScheduledExecutor scheduler;
    private final AtomicInteger jobCounter;
    private final Map<Integer, ScheduledFuture<?>> jobMap;

    /**
     * Constructor for DelayedJobScheduler class.
     * Initializes the ScheduledExecutor, AtomicInteger, and ConcurrentHashMap objects.
     */
    public DelayedJobScheduler() {
        scheduler = new ScheduledExecutor();
        jobCounter = new AtomicInteger();
        jobMap = new ConcurrentHashMap<>();
    }

    /**
     * Schedules a job to run at a specific date and time.
     *
     * @param jobName  the name of the job
     * @param dateTime the LocalDateTime when the job should run
     * @param job      the Runnable task to be executed
     * @return the unique job ID for the scheduled job
     */
    public int scheduleJob(String jobName, LocalDateTime dateTime, Runnable job) {
        int jobId = getNextJobId();
        long delay = getDelay(dateTime);
        ScheduledFuture<?> future = scheduleTask(jobName, job, delay);
        addJobToMap(jobId, future);
        return jobId;
    }

    /**
     * Generates a unique job ID for the scheduled job.
     *
     * @return the unique job ID
     */
    private int getNextJobId() {
        return jobCounter.getAndIncrement();
    }

    /**
     * Calculates the delay in milliseconds between the current time and the specified date and time.
     *
     * @param dateTime the LocalDateTime when the job should run
     * @return the delay in milliseconds
     */
    private long getDelay(LocalDateTime dateTime) {
        return ChronoUnit.MILLIS.between(LocalDateTime.now(), dateTime);
    }

    /**
     * Schedules the task using the ScheduledExecutor class.
     *
     * @param jobName the name of the job
     * @param job     the Runnable task to be executed
     * @param delay   the delay in milliseconds before the task should be executed
     * @return the ScheduledFuture object for the scheduled task
     */
    private ScheduledFuture<?> scheduleTask(String jobName, Runnable job, long delay) {
        return scheduler.schedule(jobName, job, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds the scheduled job to the ConcurrentHashMap with the job ID as the key and the ScheduledFuture object as the value.
     *
     * @param jobId  the unique job ID
     * @param future the ScheduledFuture object for the scheduled task
     */
    private void addJobToMap(int jobId, ScheduledFuture<?> future) {
        jobMap.put(jobId, future);
    }

    /**
     * Attempts to cancel the specified job by its job ID.
     *
     * @param jobId: unique jobId of the scheduled job to be cancelled
     * @return boolean: true if job was successfully cancelled, false otherwise
     */
    public boolean cancelJob(int jobId) {
        ScheduledFuture<?> future = jobMap.get(jobId);
        if (future != null) {
            if (future.cancel(false)) {
                System.out.println("Job with name " + future.getJobName() + " has been successfully cancelled.");
                return true;
            } else {
                System.out.println("Job with name " + future.getJobName() + " could not be cancelled as it has already started running.");
                return false;
            }
        } else {
            System.out.println("No job found with name " + jobId + ".");
            return false;
        }
    }

    /**
     * Shuts down the scheduler and all scheduled jobs. Any running jobs will continue to run until completion.
     * Subsequent calls to scheduleJob() or cancelJob() will fail after shutdown() is called.
     */
    public void shutdown() {
        scheduler.shutdown();
    }
}
