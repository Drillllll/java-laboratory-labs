package PT2;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;


public class TasksManager {
    private LinkedList<Runnable> runnables = new LinkedList<Runnable>();
    private static HashMap<Integer, Boolean> results = new HashMap<Integer, Boolean>();
    public Boolean isRunnablesEmpty = false;

    public synchronized void addRunnable() {
        Random random = new Random();
        Runnable newTask = () -> {
            try{
              isPrime(random.nextInt(10000000, 1000000000));
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted" + Thread.currentThread());
            }

        };

        System.out.println("New runnable added");
        runnables.add(newTask);
        isRunnablesEmpty = false;
        notify();

    }

    public synchronized static void addResult(int number, Boolean isPrime) {
        System.out.println("adding result: " + number + " " + isPrime);
        results.put(number, isPrime);
    }

    public static void isPrime(int a)  throws InterruptedException {
        Boolean result = true;
        int dividersCount = 0;
        for(int i=1; i<=a; i++) {
            if (a%i == 0)
                dividersCount++;
            if(Main.quit) {
                Thread thread = Thread.currentThread();
                thread.interrupt();
                //return;
            }
            for (int j = 0; j < 50; j++) {
                Math.sqrt(j);
            }
        }
        if (dividersCount > 2)
            result = false;
        addResult(a, result);
    }

    public synchronized Runnable getRunnable() {
        while (isRunnablesEmpty) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("getRunnable exception");
            }
        }
        if(runnables.size() == 1)
            isRunnablesEmpty = true;
        System.out.println("New runnable acquired");
        return runnables.remove(0);
    }

    public void startTasks(int n) {

        //int size = runnables.size();
        for(int i=0; i<n; i++) {
            Runnable runnable = getRunnable();
            Thread thread = new Thread(runnable);
            thread.start();
            System.out.println("New task started");
        }
    }

    public void startNewClientTask() {
        System.out.println("Trying to add new task");

        if (runnables.size() != 0) {
            Runnable runnable = getRunnable();
            Thread thread = new Thread(runnable);
            thread.start();
            System.out.println("New task started");
        }
        else
            System.out.println("no more runnables");
    }

    public void printResults() {
        for (HashMap.Entry<Integer, Boolean> entry : results.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

}
