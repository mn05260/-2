import java.io.*;
import java.nio.file.*;
import java.util.*;
public class TaskFIleManager {
    private static final String FILE_NAME = "kadai.txt";
    public static List<String> loadTasks() throws IOException{
        if (Files.exists(Paths.get(FILE_NAME))){
            return Files.readAllLines(Paths.get(FILE_NAME));
        }
        return new ArrayList<>();
    }
    public static void saveTasks(List<String> tasks) throws IOException{
        Files.write(Paths.get(FILE_NAME), tasks);
    }
    }

