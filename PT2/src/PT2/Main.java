package PT2;
import java.util.Scanner;


public class Main {
    public static volatile Boolean quit = false;
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        int threadsNumber = Integer.parseInt(args[0]);



        TasksManager tasksManager = new TasksManager();
        for(int i=0; i<7; i++) {
            tasksManager.addRunnable();
        }
        tasksManager.startTasks(threadsNumber);

        while (true) {
            String input = scanner.nextLine();

            if (!input.isEmpty()) {
                if (input.equals("q"))
                {
                    while(Thread.activeCount() != 2) {

                    }
                    System.out.println(Thread.activeCount());
                    break;
                }

                if (input.equals("n"))
                tasksManager.startNewClientTask();

            }
        }

    }
}
