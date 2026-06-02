import java.io.*;
import java.util.*;

class Memento {
    private final List<String> state;

    public Memento(List<String> state) {
        this.state = new ArrayList<>(state);
    }

    public List<String> getState() {
        return new ArrayList<>(state);
    }
}

class TextEditor {
    private List<String> lines;

    public TextEditor() {
        this.lines = new ArrayList<>();
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public void removeLastLine() {
        if (!lines.isEmpty()) {
            lines.remove(lines.size() - 1);
        }
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            sb.append(i + 1).append(": ").append(lines.get(i)).append("\n");
        }
        return sb.toString();
    }

    public Memento save() {
        return new Memento(lines);
    }

    public void restore(Memento memento) {
        this.lines = memento.getState();
    }

    public void loadFromFile(String filename) throws IOException {
        List<String> newLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                newLines.add(line);
            }
        }
        this.lines = newLines;
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}

class Caretaker {
    private final Stack<Memento> history = new Stack<>();

    public void saveState(TextEditor editor) {
        history.push(editor.save());
    }

    public void undo(TextEditor editor) {
        if (history.isEmpty()) {
            System.out.println("Нечего отменять.");
            return;
        }
        Memento previous = history.pop();
        editor.restore(previous);
        System.out.println("Отмена выполнена.");
    }
}

public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        Caretaker caretaker = new Caretaker();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Редактор текста с откатами и сохранениями");
        System.out.println("add <txt>         - добавить строку");
        System.out.println("remove            - удалить последнюю строку");
        System.out.println("undo              - отменить последнее изменение");
        System.out.println("print             - показать текст");
        System.out.println("save <file>       - сохранить текст");
        System.out.println("load <file>       - загрузить текст");
        System.out.println("exit              - выход");
        System.out.println();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "add":
                        if (parts.length < 2) {
                            System.out.println("Укажите текст для добавления.");
                            break;
                        }
                        caretaker.saveState(editor);
                        editor.addLine(parts[1]);
                        System.out.println("Строка добавлена.");
                        break;

                    case "remove":
                        caretaker.saveState(editor);
                        editor.removeLastLine();
                        System.out.println("Последняя строка удалена.");
                        break;

                    case "undo":
                        caretaker.undo(editor);
                        break;

                    case "print":
                        System.out.println("Текущий текст:");
                        String text = editor.getText();
                        if (text.isEmpty()) System.out.println("<пусто>");
                        else System.out.print(text);
                        break;

                    case "save":
                        if (parts.length < 2) {
                            System.out.println("Укажите имя файла.");
                            break;
                        }
                        editor.saveToFile(parts[1]);
                        System.out.println("Текст сохранён в файл: " + parts[1]);
                        break;

                    case "load":
                        if (parts.length < 2) {
                            System.out.println("Укажите имя файла.");
                            break;
                        }
                        caretaker.saveState(editor);
                        editor.loadFromFile(parts[1]);
                        System.out.println("Текст загружен из файла: " + parts[1]);
                        break;

                    case "exit":
                        scanner.close();
                        return;

                    default:
                        System.out.println("Неизвестная команда. Доступные: add, remove, undo, print, save, load, exit");
                }
            } catch (IOException e) {
                System.out.println("Ошибка ввода-вывода: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}