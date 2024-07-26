import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerSetup {
    public static void main(String[] args) {
        try {
            // Define the job and tie it to the ChallengeEmailJob class
            JobDetail job = JobBuilder.newJob(ChallengeEmailJob.class)
                    .withIdentity("challengeEmailJob", "group1")
                    .build();

            // Trigger the job to run every day at a specified time
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0)) // Runs daily at midnight
                    .build();

            // Schedule the job
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }
}