import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Emails {
    private Session session;
    public void sendEmail(String recipient, String subject, String body) throws FileNotFoundException {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/mail.properties"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("user"), properties.getProperty("password"));
            }
        });
        try {
            MimeMessage message = new MimeMessage(session); // Use the session initialized with SMTP settings and authentication
            message.setFrom(new InternetAddress(properties.getProperty("user")));
            message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(body);
            System.out.println("Sending email........");
            Transport.send(message);
            System.out.println("Email sent........");
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }
}