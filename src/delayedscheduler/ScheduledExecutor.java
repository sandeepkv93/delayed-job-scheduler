package delayedscheduler;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * ScheduledExecutor is a class that allows scheduling of tasks with a delay.
 * It uses a PriorityBlockingQueue to store the tasks and an Executor to execute them.
 */
public class ScheduledExecutor {
    /**
     * Executor to execute the tasks.
     */
    private final Executor executor;
    /**
     * PriorityBlockingQueue to store the tasks in order of their execution time.
     */
    private final BlockingQueue<ScheduledFuture<?>> taskQueue;

    public ScheduledExecutor() {
        executor = Executors.newFixedThreadPool(6);
        taskQueue = new PriorityBlockingQueue<>();
    }

    /**
     * Schedules a task to be executed after a specified delay.
     *
     * @param jobName the name of the job
     * @param task    the task to be executed
     * @param delay   the delay after which the task should be executed
     * @param unit    the unit of delay
     * @return ScheduledFuture representing the scheduled task
     */
    public ScheduledFuture<?> schedule(String jobName, Runnable task, long delay, TimeUnit unit) {
        ScheduledFuture<?> scheduledTask = new ScheduledFuture(jobName, new Thread(task), delay, unit);
        taskQueue.offer(scheduledTask);
        start();
        return scheduledTask;
    }

    /**
     * Starts the execution of tasks in the taskQueue.
     * Polls the taskQueue for tasks and executes them after their specified delay.
     */
    private void start() {
        executor.execute(() -> {
            while (!taskQueue.isEmpty()) {
                ScheduledFuture<?> scheduledTask = taskQueue.poll();
                long delay = scheduledTask.getDelay();
                TimeUnit unit = scheduledTask.getUnit();
                try {
                    unit.sleep(delay);
                    if (!scheduledTask.isCancelled()) {
                        System.out.println(scheduledTask.getJobName() + " started at " + LocalDateTime.now());
                        scheduledTask.getTask().run();
                        System.out.println(scheduledTask.getJobName() + " completed at " + LocalDateTime.now());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    /**
     * Shuts down the Executor.
     */
    public void shutdown() {
        ((ExecutorService) executor).shutdown();
    }
}