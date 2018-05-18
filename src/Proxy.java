import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Proxy extends Thread{

    private Socket socket;

    public Proxy(Socket socket) {
        super("ProxyThread");
        this.socket = socket;
    }

    public void run() {

        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, responseLine;
            int cnt = 0;

            String urlToCall = "";
            System.out.println(" ");
            System.out.println("REQUEST");

            while ((inputLine = in.readLine()) != " ") {
                try {
                    StringTokenizer tok = new StringTokenizer(inputLine);
                    tok.nextToken();
                    System.out.println(inputLine);
                    if (cnt == 0) {
                        urlToCall = inputLine.substring(inputLine.indexOf(' ')+1,inputLine.indexOf(' ',inputLine.indexOf(' ')+1));
                    }
                    cnt++;
                } catch (Exception e) {
                    break;
                }
            }

            try {
                if(!urlToCall.substring(0,4).equals("http")){
                    urlToCall= "http://" + urlToCall;
                }

                URL url = new URL(urlToCall);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestProperty("Content-Language", "en-US");
                con.setUseCaches(false);
                con.setDoOutput(true);

                InputStream is = con.getInputStream();
                BufferedReader bufferResponse = new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();

                while ((responseLine = bufferResponse.readLine()) != null) {
                    response.append(responseLine);
                }

                System.out.println(" ");
                System.out.println("RESPONSE");
                System.out.println(response.toString());

                String responseFinal = "HTTP/1.0 200 OK\n" + "Proxy-agent: ProxyServer/1.0\n" + "\r\n" + response.toString();
                out.write(responseFinal.getBytes());

                out.flush();
                out.close();
                socket.close();

            } catch (Exception e) {
                System.err.println("Exception: " + e);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}