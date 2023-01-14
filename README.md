# Delayed Job Scheduler

|Implementation                     |Link  |
|-----------------------------------|------|
|Using java.util.concurrent.ScheduledExecutorService| https://github.com/sandeepkv93/delayed-job-scheduler/tree/UsingJavaUtilScheduledExecutorService|
|Using custom built ScheduledExector| https://github.com/sandeepkv93/delayed-job-scheduler/tree/UsingCustomScheduledExecutor|

This repository contains a simple implementation of a delayed job scheduler in Java. The scheduler allows you to schedule jobs to run at a specific date and time, and also provides the ability to cancel scheduled jobs.

### Features
- Schedule jobs with a specific date and time
- Cancel scheduled jobs
- Shut down the scheduler

### Getting Started
To start using the delayed job scheduler, you will need to create an instance of the DelayedJobScheduler class. You can then use this instance to schedule jobs and cancel scheduled jobs.

#### Scheduling a Job
To schedule a job, you can use the scheduleJob method of the DelayedJobScheduler class. This method takes three arguments:

- The name of the job (a string)
- The date and time when the job should run (a LocalDateTime object)
- The task that should be executed when the job runs (a Runnable object)
- The method returns an integer, which is the ID of the scheduled job. You can use this ID to cancel the job later, if needed.

#### Canceling a Job
To cancel a job, you can use the cancelJob method of the DelayedJobScheduler class. This method takes a single argument: the ID of the job that you want to cancel.

#### Shutting Down the Scheduler
When you are finished scheduling jobs, you can shut down the scheduler using the shutdown method of the DelayedJobScheduler class. This method stops the scheduler's internal thread and releases any resources that it is using.

### Usage
To use the scheduler, you first need to create an instance of the DelayedJobScheduler class. You can then use the scheduleJob method to schedule a job with a specific date and time, and a Runnable task to be executed. The method returns an int jobId, which can be used to cancel the job later.
```java
DelayedJobScheduler scheduler = new DelayedJobScheduler();
LocalDateTime dateTime = LocalDateTime.now().plusSeconds(5);
int jobId = scheduler.scheduleJob("Job 1", dateTime, () -> System.out.println("Running job 1"));
```
You can also cancel a scheduled job using the cancelJob method and passing in the jobId.
```java
scheduler.cancelJob(jobId);
```
Finally, you can shut down the scheduler using the shutdown method.
```java
scheduler.shutdown();
```

### Conclusion
The delayed job scheduler is a simple, yet powerful tool that can be used to schedule tasks to run at specific times. It is easy to use, and provides the ability to cancel scheduled jobs. It is a lightweight library and easy to implement.
