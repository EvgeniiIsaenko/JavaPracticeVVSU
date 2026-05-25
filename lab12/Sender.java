import java.io.*;
import java.net.Socket;

public class Sender {
    private static final int PORT = 12345;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java FilterAndSend <input-file>");
            System.exit(1);
        }
        String filePath = args[0];
        String filteredText = readAndFilter(filePath);
        sendToServer(filteredText);
    }

    private static String readAndFilter(String filePath) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                char c = (char) ch;
                if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                    result.append(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
        return result.toString();
    }

    private static void sendToServer(String text) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(text);
            System.out.println("Sent " + text.length() + " English letters to server.");
        } catch (IOException e) {
            System.err.println("Socket error: " + e.getMessage());
            System.exit(1);
        }
    }
}