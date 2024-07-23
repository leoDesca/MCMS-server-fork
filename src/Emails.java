import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

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
        session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("user"), properties.getProperty("password"));
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("user")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(body);
            System.out.println("Sending email........");
            Transport.send(message);
            System.out.println("Email sent........");
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendEmailWithAttachment(String recipient, String subject, String body, String attachment) throws FileNotFoundException {
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
        session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("user"), properties.getProperty("password"));
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("user")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(new File(attachment).getName());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            System.out.println("Sending email with attachment........");
            Transport.send(message);
            System.out.println("Email sent with attachment........");
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }
}