//import java.util.ArrayList;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//
//
//public class PDF{
//
////a method to write a pdf file and return the file path
//    public String createPDF(ArrayList<String> questions, ArrayList<String> solutions, ArrayList<String> answers, ArrayList<String> marks, ArrayList<String> marksAwarded, int score, double timeTaken, String fullName, String email, String challengeName) {
//        String filePath = "src/"+fullName+"_"+challengeName+".pdf";
//        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream(filePath));
//            document.open();
//            document.add(new Paragraph("Challenge name: "+challengeName));
//            document.add(new Paragraph("Participant name: "+fullName));
//            document.add(new Paragraph("Participant email: "+email));
//            document.add(new Paragraph("Score: "+score));
//            document.add(new Paragraph("Time taken: "+timeTaken+" minutes"));
//            document.add(new Paragraph("Questions attempted: "));
//            for (int i=0;i<questions.size();i++){
//                document.add(new Paragraph("Question "+(i+1)+": "+questions.get(i)));
//                document.add(new Paragraph("Answer: "+solutions.get(i)));
//                document.add(new Paragraph("Correct answer: "+answers.get(i)));
//                document.add(new Paragraph("Marks: "+marks.get(i)));
//                document.add(new Paragraph("Marks awarded: "+marksAwarded.get(i)));
//            }
//            document.close();
//        } catch (DocumentException | FileNotFoundException e) {
//            System.out.println(e.getMessage());
//        }
//        return filePath;
//    }
//
//}