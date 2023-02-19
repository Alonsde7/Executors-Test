import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class Task implements Runnable {
    private String name;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void run() {
        String taskThread = "Thread " + Thread.currentThread().getId() + ": " + name;
        try {
            Long duration = (long) (Math.random() * 10);
            System.out.println(taskThread);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(taskThread + " " + e.getMessage());
        }
    }

    static void printTasks(List<Runnable> list) {
        for (Runnable r : list)
            System.out.println(((Task) r).getName() + " cancelled");
    }

    public static void main(String[] args) {
        Task task;
        int numThreads = 2;
        int numTasks = 5;
// The threadPoolExecutor instance is initialised with Executors for simplicity sake's.
// Create a pool of numThreads
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
        System.out.println("Task Executor is starting a pool of " + numThreads + " Threads and "
                + numTasks + " Tasks");
        for (int i = 1; i <= numTasks; i++) {
            task = new Task(" Task " + i);
            executor.execute(task); // execute the task by a thread of a pool thread
        }
        executor.shutdown(); // Disable new tasks from being submitted
// executor.execute( new Task ("Task6")); // This will generate RejectedExecutionException
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) { // Wait for existing tasks to terminate
                List<Runnable> list = executor.shutdownNow(); // Cancel currently executing tasks
                printTasks(list);
            }
            System.out.println("Task Executor Shutdown");
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());

        }
    }
}