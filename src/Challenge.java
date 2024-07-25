import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Challenge {


    private final String description;
    private final int duration;
    private final int questions;
    private final int wrongMark;
    private final int blankMark;
    private final String startDate;
    private final String endDate;
    private final String id;
    private  String name;

    //id getter
    public String getId() {
        return id;
    }


    //constructor

    public Challenge(String name   , String description, int duration, int questions, int wrongMark, int blankMark, String startDate, String endDate,String id) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.questions = questions;
        this.wrongMark = wrongMark;
        this.blankMark = blankMark;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id=id;
    }
    //a methods to get questions to be attempted by the participant
    public String[][] getQuestions(){
        DBO dbo = new DBO();
        dbo.connect();
        String[][]questionArray= dbo.getQuestions(id,questions);
        dbo.close();
        return questionArray;

    }

    //a method to display questions one at a time while showing time left for each attempt and storing each answer
    public void showQuestions(String[][] questionAnswers, String pEmail, int pId, String fullName){

        ArrayList<String> answers = new ArrayList<>();
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> marks = new ArrayList<>();
        ArrayList<String> marksAwarded = new ArrayList<>();
        ArrayList<String> questionIds = new ArrayList<>();
        ArrayList<String> solutions = new ArrayList<>();


        for(String[] questionAnswer:questionAnswers){
            questions.add(questionAnswer[0]);
            marks.add(questionAnswer[4]);
            answers.add(questionAnswer[1]);
            questionIds.add(questionAnswer[2]);
        }
        long startTime = System.currentTimeMillis();
        long endTime = startTime +  ( (long) duration*60000);
        double timeUsed = 0;//in minutes

        // using the date class
        SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        //loop through the questions
        for (int i=0;i<questions.size();i++){
            String sol=null;
            long remainingTime = endTime - System.currentTimeMillis();
            if(remainingTime<=0){
                timeUsed=duration;
                Main.server.printWriter.println("Time is up!!");
                //print done
//                Main.server.printWriter.println("done");
                break;
            }
            Main.server.printWriter.println("Time left: "+remainingTime/60000+" minutes and "+(remainingTime%60000)/1000+" seconds Questions left: "+(questions.size()-(i+1)));
            Main.server.printWriter.println(questions.get(i));
            try {
                sol= Main.server.reader.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if(sol!=null) sol=sol.strip();
            solutions.add(sol);


            timeUsed= (double) (System.currentTimeMillis() - startTime) /60000;
        }
        //if the solutions are less than the questions, add empty strings to the solutions
        while (solutions.size()<questions.size()){
            solutions.add("");
        }

        //  send done to the client to indicate that the challenge is done
        Main.server.printWriter.println("done");

        //calculate the score by comparing the answers to the solutions
        int score=0;
        int answered = 0;
        for (int i=0;i<solutions.size();i++){
            System.out.println(solutions.get(i)+" "+answers.get(i));
            System.out.println(solutions.get(i).equalsIgnoreCase(answers.get(i)));

            if (solutions.get(i).equalsIgnoreCase(answers.get(i))){

                score++;
                answered++;
                marksAwarded.add(marks.get(i));
            }else if (solutions.get(i).equalsIgnoreCase("")){
                score+=blankMark;
                marksAwarded.add(blankMark+"");
            }else {
                answered++;
                marksAwarded.add(wrongMark+"");
                score+=wrongMark;
            }
        }
        //if score is negative, set it to 0
        if (score<0) score=0;
        //write a pdf file with the questions and answers attempted by the participant
        //include the score and the time taken to complete the challenge
//        PDF pdf = new PDF();
//        pdf.createPDF(questions,solutions,answers,marks,marksAwarded,score,timeUsed,fullName,pEmail,name);





        //send the score to the client
        Main.server.printWriter.println("Your score is: "+score+" out of "+questions.size()+" questions and you finished in "+timeUsed+" minutes" + "You answered "+answered+"  questions");
        Emails emails = new Emails();
        String subject = "Challenge Results";
        String body = "Dear "+fullName+",\n\nYou have completed the challenge "+name+" and your score is "+score+".You finished the challenge in "+timeUsed+" minutes.\nYou answered "+answered+" questions.\n\n Thank you for attempting Regards,\nG4MCMS";
        try {
            //send email with pdf attachment
            emails.sendEmail(pEmail,subject,body);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //store each participant's score in the database
        DBO dbo = new DBO();
        dbo.connect();

        Date startDate=new Date(startTime);
        Date endDate=new Date(endTime);
        // convert the date to sql date time format
        String startDateTime = sqlDateTimeFormat.format(startDate);
        String endDateTime = sqlDateTimeFormat.format(endDate);
        //store the score in the database
        dbo.insertIntoParticipantChallenge(score,id,pId,startDateTime,endDateTime);

        //get the participant's challenge id using the participant id, challenge id and start date
        int challengeId = dbo.getParticipantChallengeId(pId,id,startDateTime);


        //inserting the answers into the database
        for (int i=0;i<solutions.size();i++){
            dbo.insertIntoParticipantAnswers(challengeId,questionIds.get(i),solutions.get(i),marksAwarded.get(i));
        }
        dbo.close();



    }
    public static void viewChallenges(){
        DBO dbo = new DBO();
        dbo.connect();
        //view all challenges
        String[] challenges= dbo.getChallenges();
        System.out.println(challenges.length);
        //if challenges is empty return no challenges

        if (challenges.length==0) Main.server.printWriter.println("no challenges");
        else {
            for (String challenge : challenges) {
                Main.server.printWriter.println(challenge);
            }
            Main.server.printWriter.println("done");
            System.out.println("done");
        }
        dbo.close();

    }

}
