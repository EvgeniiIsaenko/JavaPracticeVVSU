import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SentenceIterator implements Iterable<String> {
    private final List<String> words;

    public SentenceIterator(String text) {
        Objects.requireNonNull(text, "Text cannot be null");
        String[] parts = text.split("[\\p{Punct}\\s]+");
        words = new ArrayList<>();
        for (String part : parts) {
            if (!part.isEmpty()) {
                words.add(part);
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < words.size();
            }

            @Override
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more words in the sentence");
                }
                return words.get(currentIndex++);
            }
        };
    }

    // Example usage
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java SentenceIterator <file-path>\nOr java SentenceIterator <text>");
            System.exit(1);
        }

        String filePath = args[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                SentenceIterator sentence = new SentenceIterator(line);
                for (String word : sentence) {
                    System.out.println(word);
                }
            }
        } catch (IOException e) {
            File f = new File(filePath);

            if (!f.isFile()) {
                SentenceIterator sentence = new SentenceIterator(args[0]);
                for (String word : sentence) {
                    System.out.println(word);
                }
            } else {
                System.err.println("Error reading the file: " + e.getMessage());
            }
        }
    }
}