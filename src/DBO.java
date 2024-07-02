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

    public DBO() {
        try (FileInputStream fis = new FileInputStream("src/db.properties")) {
            Properties prop = new Properties();
            prop.load(fis);

            this.url = prop.getProperty("db.url");
            this.username = prop.getProperty("db.user");
            this.password = prop.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertParticipant(String username, String firstname, String lastname, String emailAddress, String password, String date_of_birth, String school_registration_number, String image_file) {
        query = "INSERT INTO mcms.participant (username,fname,lname, email, password, dob, schoolRegNo, image) VALUES ('" + username + "', '" + firstname + "', '" + lastname + "', '" + emailAddress + "', '" + password + "', '" + date_of_birth + "', '" + school_registration_number + "', '" + image_file + "')";
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    public void insertRejectedParticipant(String username, String firstname, String lastname, String emailAddress, String password, String date_of_birth, String school_registration_number, String image_file) {
        query = "INSERT INTO mcms.rejected_participantparticipant (username,fname,lname, email, password, dob, schoolRegNo, image) VALUES ('" + username + "', '" + firstname + "', '" + lastname + "', '" + emailAddress + "', '" + password + "', '" + date_of_birth + "', '" + school_registration_number + "', '" + image_file + "')";
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}