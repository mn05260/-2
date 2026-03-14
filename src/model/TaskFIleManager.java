package model;
import java.io.*;
import java.nio.file.*;
import java.util.*;
public class TaskFIleManager {
    private static final String FILE_NAME = "kadai.txt";
    public static List<String> loadTasks() throws IOException {
        Path path = Paths.get(FILE_NAME);
        if (Files.exists(path)) {
            return Files.readAllLines(path);
        }
        return new ArrayList<>();
    }
    public static void saveTasks(List<String> tasks) throws IOException {
        //課題に変更があった場合、上書きするようにする
        Files.write(Paths.get(FILE_NAME), tasks,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
    }
}