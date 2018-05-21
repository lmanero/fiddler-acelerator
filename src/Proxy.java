import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.util.*;

public class Proxy extends Thread{

    private Socket socket;
    private PrintWriter logPrinter;

    public Proxy(Socket socket, PrintWriter logPrinter) {
        super("ProxyThread");
        this.socket = socket;
        this.logPrinter = logPrinter;
    }

    public void run() {

        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine, responseLine, urlToCall = "";
            int cnt = 0;

            logPrinter.println(" ");
            logPrinter.println("REQUEST:");
            while ((inputLine = in.readLine()) != " ") {
                try {
                    StringTokenizer tok = new StringTokenizer(inputLine);
                    tok.nextToken();
                    logPrinter.println(inputLine);
                    if (cnt == 0) {
                        urlToCall = inputLine.substring(inputLine.indexOf(' ')+1,inputLine.indexOf(' ',inputLine.indexOf(' ')+1));
                    }
                    cnt++;
                } catch (Exception e) {
                    break;
                }
            }
            if(urlToCall.length() > 4 && !urlToCall.substring(0,4).equals("http")){
                urlToCall= "http://" + urlToCall;
            }
            try {
                URL url = new URL(urlToCall);
                if((urlToCall.contains(".png")) || urlToCall.contains(".jpg") ||
                        urlToCall.contains(".jpeg") || urlToCall.contains(".gif")){
                    BufferedImage image = ImageIO.read(url);
                    if(image != null) {
                        String line = "HTTP/1.0 200 OK\n" +
                                "Proxy-agent: ProxyServer/1.0\n" +
                                "\r\n";
                        out.write(line.getBytes());
                        out.flush();
                        ImageIO.write(image, urlToCall.substring(urlToCall.lastIndexOf(".")+1, urlToCall.length()), out);
                    } else {
                        String error = "HTTP/1.0 404 NOT FOUND\n" +
                                "Proxy-agent: ProxyServer/1.0\n" +
                                "\r\n";
                        out.write(error.getBytes());
                    }
                }else{
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
                    logPrinter.println(" ");
                    logPrinter.println("RESPONSE:");
                    logPrinter.println(response.toString());
                    String responseFinal = "HTTP/1.0 200 OK\n" + "Proxy-agent: ProxyServer/1.0\n" + "\r\n" + response.toString();
                    out.write(responseFinal.getBytes());
                }
                out.flush();
            } catch (Exception e) {
                System.err.println("Exception: " + e);
            } finally {
                if (out != null) out.close();
                if (in != null) in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
