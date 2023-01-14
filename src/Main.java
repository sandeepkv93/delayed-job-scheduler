import java.time.LocalDateTime;
import delayedscheduler.DelayedJobScheduler;

public class Main {
    public static void main(String[] args) {
        // Create a new instance of DelayedJobScheduler
        DelayedJobScheduler scheduler = new DelayedJobScheduler();

        // Schedule job 1 to run in 5 seconds
        LocalDateTime dateTime1 = LocalDateTime.now().plusSeconds(5);
        int jobId1 = scheduler.scheduleJob("Job 1", dateTime1, () -> System.out.println("Running job 1"));

        // Schedule job 2 to run in 10 seconds
        LocalDateTime dateTime2 = LocalDateTime.now().plusSeconds(10);
        int jobId2 = scheduler.scheduleJob("Job 2", dateTime2, () -> System.out.println("Running job 2"));

        // Schedule job 3 to run in 15 seconds
        LocalDateTime dateTime3 = LocalDateTime.now().plusSeconds(15);
        int jobId3 = scheduler.scheduleJob("Job 3", dateTime3, () -> System.out.println("Running job 3"));

        // Schedule job 4 to run in 20 seconds
        LocalDateTime dateTime4 = LocalDateTime.now().plusSeconds(20);
        int jobId4 = scheduler.scheduleJob("Job 4", dateTime4, () -> System.out.println("Running job 4"));

        // Cancel job 3
        scheduler.cancelJob(jobId3);

        // Shutdown the scheduler
        scheduler.shutdown();
    }
}