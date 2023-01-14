package delayedscheduler;

import java.time.LocalDateTime;
import java.util.concurrent.*;

public class ScheduledExecutor {
    private final Executor executor;
    private final BlockingQueue<ScheduledFuture<?>> taskQueue;

    public ScheduledExecutor() {
        executor = Executors.newFixedThreadPool(6);
        taskQueue = new PriorityBlockingQueue<>();
    }

    public ScheduledFuture<?> schedule(String jobName, Runnable task, long delay, TimeUnit unit) {
        ScheduledFuture<?> scheduledTask = new ScheduledFuture(jobName, new Thread(task), delay, unit);
        taskQueue.offer(scheduledTask);
        start();
        return scheduledTask;
    }

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

    public void shutdown() {
        ((ExecutorService) executor).shutdown();
    }
}