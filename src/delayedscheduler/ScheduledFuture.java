package delayedscheduler;

import java.sql.Time;
import java.util.concurrent.*;

public class ScheduledFuture<V> implements Future<V>, Comparable<ScheduledFuture> {
    private final Thread task;

    private final long delay;

    public String getJobName() {
        return jobName;
    }

    private final String jobName;

    public Thread getTask() {
        return task;
    }

    public long getDelay() {
        return delay;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    private final TimeUnit unit;

    private boolean isCancelled;
    private boolean isDone;
    private V result;

    public ScheduledFuture(String jobName, Thread task, long delay, TimeUnit unit) {
        this.jobName = jobName;
        this.task = task;
        this.delay = delay;
        this.unit = unit;
    }

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

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

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

    public void setResult(V result) {
        this.result = result;
    }

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