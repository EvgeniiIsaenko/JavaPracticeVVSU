import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);
            while (true) { 
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(clientSocket.getInputStream(), "UTF-8"))) {
                    String received = in.readLine();
                    if (received != null) {
                        System.out.println("Received text (only English letters):");
                        System.out.println(received);
                    }
                } catch (IOException e) {
                    System.err.println("Connection error: " + e.getMessage());
                }
                //break; //будет бесконечно слушать
            }
        } catch (IOException e) {
            System.err.println("Server socket error: " + e.getMessage());
        }
    }
}