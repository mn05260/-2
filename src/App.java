import java.io.PrintStream;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        
        System.setOut(new PrintStream(System.out, true, "UTF-8"));

        
        Scanner scanner = new Scanner(System.in, "UTF-8");

        System.out.println("--- TO DO app ---");
        System.out.print("Enter a task: ");
        
        String task = scanner.nextLine();
        
        System.out.println("Registered task: " + task);
        
        scanner.close();
    }
}