import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket=null;
    private Socket socket=null;
    protected BufferedReader reader=null;
    protected PrintWriter printWriter=null;
    private FileWriter fileWriter=null;
    private BufferedWriter bufferedFileWriter=null;
    private BufferedReader fileReader=null;

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
        try {
            fileWriter=new FileWriter("src/participants.txt",true);
            bufferedFileWriter = new BufferedWriter(fileWriter);
            bufferedFileWriter.write(request[1]+" "+request[2]+" "+request[3]+" "+request[4]+" "+request[5]+" "+request[6]+" "+request[7]+" "+request[8]);
            bufferedFileWriter.newLine();
            bufferedFileWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }



    }

    public void login(String[] request) {
        DBO dbo = new DBO();
        dbo.connect();
        if (dbo.checkParticipant(request[1], request[2])) printWriter.println("participant "+request[1]);
        else if (dbo.checkRepresentative(request[1], request[2])) printWriter.println("representative "+request[1]);
        else printWriter.println("invalid");



    }
}