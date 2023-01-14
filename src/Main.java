import java.time.LocalDateTime;
import delayedscheduler.DelayedJobScheduler;

public class Main {
    public static void main(String[] args) {
        DelayedJobScheduler scheduler = new DelayedJobScheduler();
        LocalDateTime dateTime1 = LocalDateTime.now().plusSeconds(5);
        int jobId1 = scheduler.scheduleJob("Job 1", dateTime1, () -> System.out.println("Running job 1"));

        LocalDateTime dateTime2 = LocalDateTime.now().plusSeconds(10);
        int jobId2 = scheduler.scheduleJob("Job 2", dateTime2, () -> System.out.println("Running job 2"));

        LocalDateTime dateTime3 = LocalDateTime.now().plusSeconds(15);
        int jobId3 = scheduler.scheduleJob("Job 3", dateTime3, () -> System.out.println("Running job 3"));

        LocalDateTime dateTime4 = LocalDateTime.now().plusSeconds(20);
        int jobId4 = scheduler.scheduleJob("Job 4", dateTime4, () -> System.out.println("Running job 4"));

        // Cancel job 3
        scheduler.cancelJob(jobId3);

        scheduler.shutdown();
    }
}