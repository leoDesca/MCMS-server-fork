import java.io.IOException;
import java.util.Arrays;

public class Main {
    static Server server=null;
    static String[] request;
   static  String[] initialCommands;
    //handles the

    public static void main(String[] args) {
        Main main = new Main();

        main.server=new Server(3333);
        main.initialCommands = new String[]{"register", "login"};


        try {
            while(true) {
                request = server.reader.readLine().strip().split(" ");

                server.printWriter.println("command received");

                System.out.println(Arrays.toString(request));
                if (Arrays.asList(initialCommands).contains(request[0])) {


                    switch (request[0]) {
                        case "register":
                            if (request.length >= 8 && request.length<=9) {
                                server.printWriter.println("valid command");

                                server.register(request);
                            }else {
                                server.printWriter.println("Invalid command");
                            }
                            break;
                        case "login":
                            if (request.length == 3) {
                                server.printWriter.println("valid command");

                                server.login(request);
                            }else {
                                server.printWriter.println("Incomplete command. please enter all the required fields");
                            }
                            break;
                        case "exit":
                            server.close();
                            break;
                        default:
                            server.printWriter.println("Invalid command");
                    }
                } else {
                    System.out.println("Invalid command");

                    server.printWriter.println("Invalid command");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}
