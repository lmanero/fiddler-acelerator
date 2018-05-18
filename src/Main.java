
import java.io.*;
import java.net.*;

public class Main {

    public static void main(String[] args){

        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(8888);
            while(true){
                new Proxy(serverSocket.accept()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}




