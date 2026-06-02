import java.io.*;
import java.nio.file.*;

// https://en.wikipedia.org/wiki/Singleton_pattern
// volatile для потокобезопасности и записи актуальных настроек
// в получении инстанса синхронизация потоков чтобы друг на друга не залазили
public class AppSettings {
    private static final String SETTINGS_FILE = "AppSettings.txt";

    private volatile String language;
    private volatile String outputFile;
    private volatile boolean feature1;
    private volatile boolean feature2;

    // только один экземпляр
    private static volatile AppSettings instance;

    private AppSettings() {
        loadSettings();
    }

    public static AppSettings getInstance() {
        if (instance == null) {
            synchronized (AppSettings.class) {
                if (instance == null) {
                    instance = new AppSettings();
                }
            }
        }
        
        return instance;
    }

    private void loadSettings() {
        Path filePath = Paths.get(SETTINGS_FILE);
        if (!Files.exists(filePath)) {
            // дефолт значения
            language = "en";
            outputFile = "output.txt";
            feature1 = true;
            feature2 = true;
            saveSettings(); 

            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            language = reader.readLine();
            if (language == null) throw new IOException("Недостаточно строк в файле");
            
            outputFile = reader.readLine();
            if (outputFile == null) throw new IOException("Недостаточно строк в файле");
            
            feature1 = Boolean.parseBoolean(reader.readLine());
            feature2 = Boolean.parseBoolean(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка чтения файла настроек. Используются значения по умолчанию.");
            language = "en";
            outputFile = "output.txt";
            feature1 = true;
            feature2 = true;
            saveSettings();
        }
    }

    public synchronized void saveSettings() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SETTINGS_FILE))) {
            writer.write(language);
            writer.newLine();
            writer.write(outputFile);
            writer.newLine();
            writer.write(Boolean.toString(feature1));
            writer.newLine();
            writer.write(Boolean.toString(feature2));
        } catch (IOException e) {
            System.err.println("Ошибка сохранения настроек: " + e.getMessage());
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public boolean isFeature1() {
        return feature1;
    }

    public void setFeature1(boolean feature1) {
        this.feature1 = feature1;
    }

    public boolean isFeature2() {
        return feature2;
    }

    public void setFeature2(boolean feature2) {
        this.feature2 = feature2;
    }

    public static void main(String[] args) {
        AppSettings settings1 = AppSettings.getInstance();
        AppSettings settings2 = AppSettings.getInstance();

        System.out.println("settings1 == settings2 ? " + (settings1 == settings2)); // true

        System.out.println("Исходный язык: " + settings1.getLanguage());
        settings1.setLanguage("ru");
        System.out.println("Язык после изменения через settings1: " + settings1.getLanguage());
        System.out.println("Язык, прочитанный через settings2: " + settings2.getLanguage());

        settings1.saveSettings();
        System.out.println("Настройки сохранены в файл " + SETTINGS_FILE);

        System.out.println("\nСодержимое файла настроек:");
        try {
            Files.lines(Paths.get(SETTINGS_FILE)).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}