
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {

    public static void main(String[] args){

        ServerSocket serverSocket;

        try {
            String timeLog = new SimpleDateFormat("yyyy-MM-dd").format(
                    Calendar.getInstance().getTime());
            PrintWriter writer = new PrintWriter(timeLog+"_logs", "UTF-8");
            writer.println("LOGS OF: " + timeLog);
            serverSocket = new ServerSocket(8888);
            System.out.println("Starting PROXY on port: 8888");
            while(true){
                new Proxy(serverSocket.accept(), writer).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}




