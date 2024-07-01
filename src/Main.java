import java.io.IOException;
import java.util.Arrays;

public class Main {
    Server server=null;
    String[] request;
    String[] initialCommands;
    //handles the
    public void start(){

        // an array of the expected commands when the server is started

        try {
            request = server.reader.readLine().strip().split(" ");
//            System.out.println(Arrays.toString(request));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.server=new Server(3333);
        main.initialCommands = new String[]{"register", "login"};
        main.start();

    }
}
