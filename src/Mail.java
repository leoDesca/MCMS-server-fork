import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

//this class shall handle the email sending
public class Mail {
    //    private final String username = "
    Properties prop = null;
    Session session = null;
    Transport transport = null;
    Message message = null;

    public Mail() {
        prop = new Properties();
        prop.put("mail.user", "g4mcms256@gmail.com");
        prop.put("mail.password", "art272727.");
        prop.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        prop.put("mail.smtp.port", "587"); // TLS Port
        prop.put("mail.smtp.auth", "true"); // Enable authentication
        prop.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("g4mcms256@gmail.com", "art272727.");
            }
        });
    }

    public void sendMail(String schoolEmail, String subject, String body) {
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(prop.getProperty("mail.user")));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(schoolEmail));
            message.setSubject(subject);
            message.setText(body);
            transport = session.getTransport("smtp");
            transport.connect(prop.getProperty("mail.smtp.host"), prop.getProperty("mail.user"), prop.getProperty("mail.password"));
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            System.out.println(e.getMessage());

        }
    }
}