import javax.imageio.ImageIO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.List;

public class Server {
    private ServerSocket serverSocket=null;
    private Socket socket=null;
    protected BufferedReader reader=null;
    protected PrintWriter printWriter=null;
    protected FileWriter fileWriter=null;
    protected BufferedWriter bufferedFileWriter=null;


    Server(int port){
        try {
            //creating server
            serverSocket=new ServerSocket(port);
            System.out.println("server created successfully!!");
            System.out.println("Listening");

            //listening for requests
                    socket=serverSocket.accept();
            System.out.println("Connected!!");
            //configuring input
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // configuring output stream
            printWriter=new PrintWriter(socket.getOutputStream(),true);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
// closing the server
    public void close(){
        try {
            serverSocket.close();
            printWriter.close();
            reader.close();
            socket.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
            return;
        }
    }
    public boolean register(String[] request) {
        DBO dbo=new DBO();
        dbo.connect();
//check if the school exists
        if (dbo.checkSchoolExists(request[7])){
            try {
                fileWriter=new FileWriter("src/participants.txt",true);
                bufferedFileWriter = new BufferedWriter(fileWriter);
                bufferedFileWriter.write(request[1]+" "+request[2]+" "+request[3]+" "+request[4]+" "+ hashPassword(request[5])+" "+request[6]+" "+request[7]+" "+request[8]);
                bufferedFileWriter.newLine();
                bufferedFileWriter.close();
                fileWriter.close();
                Emails emails = new Emails();
                emails.sendEmail(request[4],"Registration","You have successfully registered for the competition\n Please wait for confirmation email from your school representative\n\nRegards,\nG4MCMS");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            dbo.close();

            return true;
        } else return false;
    }
// login method
    public void login(String[] request) {
        DBO dbo = new DBO();
        dbo.connect();
        if (dbo.checkParticipant(request[1],hashPassword(request[2]) )){
            printWriter.println("participant "+request[1]);
            Participant participant = new Participant(dbo.getParticipantDetails(request[1]));
            participant.start();
            dbo.close();
        }
        else if (dbo.checkRepresentative(request[1],hashPassword(request[2]) )) {
            printWriter.println("representative "+request[1]);
            Representative rep = new Representative(dbo.getRepresentative(request[1]));
            rep.validateParticipant();
            dbo.close();
        }
        else printWriter.println("invalid");
    }
    //a method to hash the password
    public String hashPassword(String password){
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            hashedPassword= sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }
    //a method to check that username and email are not in the file participants.txt already using a file reader
    public boolean checkUsernameFile(String username) {
        String line;
        try {
            BufferedReader bufferedFileReader = new BufferedReader(new FileReader("src/participants.txt"));
            while ((line = bufferedFileReader.readLine()) != null) {
                if (line.split(" ")[0].equals(username)) return true;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public byte[] imageToByteArray(String imagePath) {
        try {
            BufferedImage bImage = ImageIO.read(new File(imagePath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos);
            return bos.toByteArray();
        } catch (IOException e) {
            System.out.println("Error converting image to byte array: " + e.getMessage());
            return null;
        }
    }
     public void sendReminderEmails() {
    // Code to retrieve participants who missed the challenge deadline and send them emails
    DBO dbo = new DBO();
    dbo.connect();
    
    // Retrieve list of participants who missed deadlines
    List<String> participants = dbo.getParticipantsWithMissedDeadlines();
    
    Emails emails = new Emails();
    for (String participant : participants) {
        // Customize these parameters as needed
        String recipient = dbo.getEmailForParticipant(participant);
        String subject = "Challenge Deadline Missed";
        String body = "Dear " + participant + ",\n\nYou missed the deadline for the challenge.\n\nRegards,\nG4MCMS";
        try {
            emails.sendEmail(recipient, subject, body);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
} 

     //method to create and send pdfs.
     private static String createPdf(String username, int score, String questionsAttempted, String answersGiven, String timeTaken) throws IOException {
        String pdfFilePath = "C:\\Users\\J\\OneDrive\\Desktop\\PDF" + username + "_challenge_details.pdf";

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
            document.open();
            document.add(new Paragraph("Challenge Attempt Details"));
            document.add(new Paragraph("Username: " + username));
            document.add(new Paragraph("Score: " + score));
            document.add(new Paragraph("Time Taken: " + timeTaken));
            document.add(new Paragraph("Questions Attempted:\n" + questionsAttempted));
            document.add(new Paragraph("Answers Given:\n" + answersGiven));
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return pdfFilePath;
    }

}