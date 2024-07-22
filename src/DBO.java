import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBO {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String query;
    private String url;
    private String username;
    private String password;
//method to connect to the database
    public DBO() {
        try (FileInputStream fis = new FileInputStream("src/db.properties")) {
            Properties prop = new Properties();
            prop.load(fis);

            this.url = prop.getProperty("db.url");
            this.username = prop.getProperty("db.user");
            this.password = prop.getProperty("db.password");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

//method to check if a participant exists in the database
    public boolean checkParticipant(String username, String password) {
        query = "SELECT * FROM participant WHERE username = '" + username + "' AND password = '" + password + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
//method to check if a school representative exists in the database
    public boolean checkRepresentative(String username, String password) {
        query = "SELECT * FROM school_representative WHERE rep_username = '" + username + "' AND rep_password = '" + password + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
//method to get the details of a representative
    public String[] getRepresentative(String username) {
        query = "SELECT * FROM school_representative WHERE rep_username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            return new String[]{resultSet.getString("rep_username"), resultSet.getString("rep_fname"), resultSet.getString("rep_lname"), resultSet.getString("rep_email"), resultSet.getString("school_regNo")};
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //method to check for school registration number

    public boolean checkSchoolExists(String school) {
        query = "SELECT * FROM school_representative WHERE school_regNo = '" + school + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
//method to insert a participant into the database
    public void insertParticipant(String[] details, byte[] image) {
        query = "INSERT INTO participant (username, fname, lname, email, password, dob, schoolRegNo, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, details[0]);
            pstmt.setString(2, details[1]);
            pstmt.setString(3, details[2]);
            pstmt.setString(4, details[3]);
            pstmt.setString(5, details[4]);
            pstmt.setString(6, details[5]);
            pstmt.setString(7, details[6]);
            pstmt.setBytes(8, image);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
//method to insert a rjected participant into the rejected_participant table
    public void insertRejectedParticipant(String[] details, byte[] image) {
        query = "INSERT INTO rejected_participant (username, fname, lname, email, password, dob, schoolRegNo, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, details[0]);
            pstmt.setString(2, details[1]);
            pstmt.setString(3, details[2]);
            pstmt.setString(4, details[3]);
            pstmt.setString(5, details[4]);
            pstmt.setString(6, details[5]);
            pstmt.setString(7, details[6]);
            pstmt.setBytes(8, image);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //method to check whether username is in either participant tables or school_representative table
    public boolean checkUsername(String username) {
        query = "SELECT * FROM participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return true;
            query = "SELECT * FROM school_representative WHERE rep_username = '" + username + "'";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) return true;
            query = "SELECT * FROM rejected_participant WHERE username = '" + username + "'";
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //method to check whether a participant has been registered in either rejected_participant table
    public boolean checkRejectedParticipant(String username) {
        query = "SELECT * FROM rejected_participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //method to  get an email from mcmcs.participant and laravelll.school_represenatative that takes username
    public String getEmail(String username) {
        query = "SELECT email FROM participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getString("email");
            query = "SELECT rep_email FROM school_representative WHERE rep_username = '" + username + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getString("rep_email");
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    //method to get the image of a participant
    public byte[] getImage(String username) {
        query = "SELECT image FROM participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getBytes("image");
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //method to get the image of a rejected participant
    public byte[] getRejectedImage(String username) {
        query = "SELECT image FROM laravelll.rejected_participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getBytes("image");
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //method to get the image of a school representative
    public byte[] getRepImage(String username) {
        query = "SELECT image FROM laravelll.school_representative WHERE rep_username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getBytes("image");
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    // method to get the rep username using school reg number
    public String getRepUsername(String school) {
        query = "SELECT rep_username FROM laravelll.school_representative WHERE school_regNo = '" + school + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getString("rep_username");
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //a method to get all challenge names   from the database where the start date is less than the current date and the end date is greater than the current date
    public String[] getChallenges()  {

        query = "SELECT * FROM laravelll.challenge WHERE challenge.challenge_start_date < CURDATE() AND challenge.challenge_end_date > CURDATE();";
        try {
            resultSet = statement.executeQuery(query);
            //return null if resultset is empty
            if (!resultSet.next()) return null;
            resultSet.last();
            String[] challenges = new String[resultSet.getRow()];
            resultSet.beforeFirst();
            int i = 0;
            while (resultSet.next()) {
                challenges[i] = "Challenge name : "+resultSet.getString("challenge_name").toUpperCase()+"\nChallenge Description: " +resultSet.getString("challenge_description")+"\nChallenge Start date: "+resultSet.getString("challenge_start_date")+"\nChallenge end date: "+resultSet.getString("challenge_end_date");
                i++;
            }
            return challenges;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    //get participant details
    public String[] getParticipantDetails(String username) {
        query = "SELECT * FROM laravelll.participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            return new String[]{resultSet.getString("username"), resultSet.getString("fname"), resultSet.getString("lname"), resultSet.getString("email"), resultSet.getString("dob"), resultSet.getString("schoolRegNo"), resultSet.getString("participant_id")};
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    //check challenge exists
    public boolean checkChallengeExists(String challengeName) {
        query = "SELECT * FROM laravelll.challenge WHERE challenge_name = '" + challengeName + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
//get challenge details 
    public String[] getChallengeDetails(String challengeName) {
        query = "SELECT * FROM laravelll.challenge WHERE challenge_name = '" + challengeName + "'";
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            return new String[]{resultSet.getString("challenge_name"), resultSet.getString("challenge_description"), resultSet.getString("duration"), resultSet.getString("questions_to_answer"),resultSet.getString("wrong_answer_marks"), resultSet.getString("blank_answer_marks"), resultSet.getString("challenge_start_date"), resultSet.getString("challenge_end_date"), resultSet.getString("challenge_id")};
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
//get random questions and their answers from question table where challenge id is equal to the challenge id
    public String[][] getQuestions(String id, int questions) {

        query = "SELECT * FROM laravelll.question WHERE challenge_id = '" + id + "' ORDER BY RAND() LIMIT " + questions;
        try {
            resultSet = statement.executeQuery(query);
            resultSet.last();
            String[][] questionArray = new String[resultSet.getRow()][5];
            resultSet.beforeFirst();
            int i = 0;
            while (resultSet.next()) {
                questionArray[i][0] = resultSet.getString("question");
                questionArray[i][1] = resultSet.getString("answer");
                questionArray[i][2] = resultSet.getString("question_id");
                questionArray[i][3] = resultSet.getString("challenge_id");
                questionArray[i][4] = resultSet.getString("marks");
                i++;
            }
            return questionArray;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
    }
//insert into participant challenge table
    public void insertIntoParticipantChallenge(int score, String cId, int pId, String startDateTime, String endDateTime) {
        query = "INSERT INTO laravelll.participant_challenge (score, challenge_id, participant_id, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, score);
            pstmt.setInt(2, Integer.parseInt(cId));
            pstmt.setInt(3, pId);
            pstmt.setString(4, startDateTime);
            pstmt.setString(5, endDateTime);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //get the participant challenge id
    public int getParticipantChallengeId(int pId, String cId, String startDateTime) {
        query = "SELECT participant_challenge_id FROM laravelll.participant_challenge WHERE participant_id = " + pId + " AND challenge_id = " + cId + " AND start_time = '" + startDateTime + "'";
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            return resultSet.getInt("participant_challenge_id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
//insert into participant answers table

    public void insertIntoParticipantAnswers(int challengeId, String qId, String answer, String marks) {
        query = "INSERT INTO laravelll.participant_answer (participant_answer.participant_challenge_id, question_id, answer,marks) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, challengeId);
            pstmt.setInt(2, Integer.parseInt(qId));
            pstmt.setString(3, answer);
            pstmt.setInt(4, Integer.parseInt(marks));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
