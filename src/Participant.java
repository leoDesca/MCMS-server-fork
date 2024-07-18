import java.io.IOException;
import java.time.LocalDateTime;

public class Participant {
    private final String username;
    private final String fullName;
    private final String email;
    private int participantId;

    public Participant(String[] details) {

         this.username = details[0];
        this.email = details[3];
        this.fullName= details[1].toUpperCase()+" "+details[2].toUpperCase();
        this.participantId=Integer.parseInt(details[6]);

    }




    public void start() {
        String command;
        System.out.println("Start for participant........");
        try {
        command=Main.server.reader.readLine().strip();
            System.out.println(command);


            if (command.equalsIgnoreCase("view challenges")) {
                System.out.println("Viewing challenges");
                Challenge.viewChallenges();
                start();
            }
            else if (command.equalsIgnoreCase("attempt challenge")){
                System.out.println("Attempting challenge");
                Challenge.viewChallenges();
                command=Main.server.reader.readLine().strip();
                DBO dbo = new DBO();
                dbo.connect();
                if(dbo.checkChallengeExists(command)) {
                    String[] challengeDetails = dbo.getChallengeDetails(command);

                    String[] details = {"Challenge name: " + challengeDetails[0], "Challenge description: " + challengeDetails[1], "Challenge duration(minutes): " + challengeDetails[2], "Questions to answer: " + challengeDetails[3], "Marks for wrong answer: " + challengeDetails[4], "Marks for correct answer: 1", "Marks for unanswered question: " + challengeDetails[5], "Start date: " + challengeDetails[6], "End date: " + challengeDetails[7]};
                    Challenge challenge = new Challenge(challengeDetails[0], challengeDetails[1], Integer.parseInt(challengeDetails[2]), Integer.parseInt(challengeDetails[3]), Integer.parseInt(challengeDetails[4]), Integer.parseInt(challengeDetails[5]), challengeDetails[6], challengeDetails[7], challengeDetails[8]);
                    for (String detail : details) {
                        Main.server.printWriter.println(detail);
                    }
                    //send done to the client
                    Main.server.printWriter.println("done");
                    //receive the response from the client
                    String response = Main.server.reader.readLine().strip();
                    System.out.println(response);
                    //if the response is start, send the questions to the client
                    switch (response) {
                        case "start":
                            System.out.println("Starting.....");
                            challenge.showQuestions(challenge.getQuestions(), email, participantId, fullName);
                            start();
                            //if the response is back listen for new participant choice
                            break;
                        case "back":
                            System.out.println("going back");
                            start();
                            break;

                        default:
                            Main.server.printWriter.println("invalid command");
                            start();
                    }
                }
                else {
                    Main.server.printWriter.println("invalid challenge");
                    start();
                }
            } else if ( command.equalsIgnoreCase("exit")){
                System.out.println("in exit");
                Main.start();
            } else {
                Main.server.printWriter.println("invalid command");
                start();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
