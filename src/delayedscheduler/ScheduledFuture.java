package delayedscheduler;

import java.util.concurrent.*;

/**
 * Class that represents a scheduled task that will be executed in the future.
 * Implements the Future and Comparable interfaces to allow for cancelling and comparing tasks.
 *
 * @param <V> the type of the result of the task
 */
public class ScheduledFuture<V> implements Future<V>, Comparable<ScheduledFuture> {
    // The task to be executed
    private final Thread task;
    // The delay before the task is executed
    private final long delay;
    // The name of the job
    private final String jobName;
    // The time unit of the delay
    private final TimeUnit unit;
    // Flag to indicate if the task has been cancelled
    private volatile boolean isCancelled;
    // Flag to indicate if the task has completed
    private volatile boolean isDone;
    // The result of the task
    private V result;

    /**
     * Constructor for ScheduledFuture class.
     *
     * @param jobName the name of the job
     * @param task    the task to be executed
     * @param delay   the delay before the task is executed
     * @param unit    the time unit of the delay
     */
    public ScheduledFuture(String jobName, Thread task, long delay, TimeUnit unit) {
        this.jobName = jobName;
        this.task = task;
        this.delay = delay;
        this.unit = unit;
    }

    /**
     * getJobName() method retrieves the job name associated with this ScheduledFuture instance
     *
     * @return jobName the name of the job
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * getTask() method retrieves the task associated with this ScheduledFuture instance
     *
     * @return task the task of the job
     */
    public Thread getTask() {
        return task;
    }

    /**
     * getDelay() method retrieves the delay associated with this ScheduledFuture instance
     *
     * @return delay the delay of the job
     */
    public long getDelay() {
        return delay;
    }

    /**
     * getUnit() method retrieves the time unit associated with this ScheduledFuture instance
     *
     * @return unit the time unit of the delay
     */
    public TimeUnit getUnit() {
        return unit;
    }

    /**
     * Override of the cancel method from the Future interface.
     * Cancels the scheduled task and sets the isCancelled and isDone flags to true.
     * If the task is already done, it will not be cancelled and the method will return false.
     *
     * @param mayInterruptIfRunning - flag to indicate if the task should be interrupted if it is running
     * @return - true if the task was successfully cancelled, false otherwise
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (!isDone) {
            isCancelled = true;
            isDone = true;
            task.interrupt();
            return true;
        }
        return false;
    }

    /**
     * Override of the isCancelled method from the Future interface.
     * Returns the value of the isCancelled flag
     *
     * @return - true if the task was cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Override of the isDone method from the Future interface.
     * Returns the value of the isDone flag
     *
     * @return - true if the task was completed or cancelled, false otherwise
     */
    @Override
    public boolean isDone() {
        return isDone;
    }

    /**
     * Overrides the Future.get() method. If the task is not cancelled or done yet, it waits for the task to complete
     * and sets the isDone flag to true. If the task is cancelled, it throws a CancellationException. Otherwise, it
     * returns the result of the task.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting for the task to complete
     * @throws ExecutionException   if the computation threw an exception
     */
    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (!isCancelled && !isDone) {
            task.join();
            isDone = true;
            return result;
        } else if (isCancelled) {
            throw new CancellationException();
        } else {
            return result;
        }
    }

    /**
     * Overrides the Future.get(long timeout, TimeUnit unit) method. If the task is not cancelled or done yet, it waits
     * for the task to complete or for the specified timeout to expire. If the task completes within the timeout, it
     * sets the isDone flag to true and returns the result. If the timeout expires, it throws a TimeoutException. If
     * the task is cancelled, it throws a CancellationException. Otherwise, it returns the result of the task.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @throws InterruptedException if the current thread is interrupted while waiting for the task to complete
     * @throws ExecutionException   if the computation threw an exception
     * @throws TimeoutException     if the timeout expires before the task completes
     */
    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!isCancelled && !isDone) {
            task.join(unit.toMillis(timeout));
            if (!task.isAlive()) {
                isDone = true;
                return result;
            } else {
                throw new TimeoutException();
            }
        } else if (isCancelled) {
            throw new CancellationException();
        } else {
            return result;
        }
    }

    /**
     * Sets the result of the task.
     *
     * @param result The result of the task.
     */
    public void setResult(V result) {
        this.result = result;
    }

    /**
     * Compares this ScheduledFuture with the specified ScheduledFuture for order. Returns a
     * negative integer, zero, or a positive integer as this object's delay is less than, equal
     * to, or greater than the specified object's delay.
     *
     * @param other The ScheduledFuture to be compared.
     * @return a negative integer, zero, or a positive integer as this object's delay is less
     * than, equal to, or greater than the specified object's delay.
     */
    @Override
    public int compareTo(ScheduledFuture other) {
        long difference = this.delay - other.delay;
        if (difference < 0) {
            return -1;
        } else if (difference > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
