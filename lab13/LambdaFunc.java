import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@FunctionalInterface
interface StringBiFunction {
    String apply(String s1, String s2);
}

public class LambdaFunc {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java LambdaFunc <file-path>");
            System.exit(1);
        }

        String filePath = args[0];

        // лямбда функция
        StringBiFunction longerString = (s1, s2) ->
                (s1.length() >= s2.length()) ? s1 : s2;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String longestLine = null;
            String line;

            while ((line = reader.readLine()) != null) {
                if (longestLine == null) {
                    longestLine = line;
                } else {
                    longestLine = longerString.apply(longestLine, line);
                }
            }

            if (longestLine != null) {
                System.out.println("Longest string in the file:");
                System.out.println(longestLine); // chcp 65001
                System.out.println("Length: " + longestLine.length() + " symbols.");
            } else {
                System.out.println("Empry file");
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}