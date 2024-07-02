import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket=null;
    private Socket socket=null;
    protected BufferedReader reader=null;
    protected PrintWriter printWriter=null;

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

    private void handleClient(Socket socket) {

    }

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

    public void register(String[] request) {
    }

    public void login(String[] request) {
        System.out.println("lojlhjyfhydsgrg");
    }
}