import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ChallengeEmailJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Code to send emails to participants who missed the challenge deadline
        Server server = new Server(3333); // Port should match your configuration
        server.sendReminderEmails();
    }
}
