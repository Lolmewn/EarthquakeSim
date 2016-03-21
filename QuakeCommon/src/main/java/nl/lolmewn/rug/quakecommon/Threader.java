package nl.lolmewn.rug.quakecommon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Threading utility class
 *
 * @author Lolmewn
 */
public class Threader {

    private final static ExecutorService EXECUTOR;

    static {
        EXECUTOR = Executors.newCachedThreadPool();
    }

    private Threader() {
        // Don't allow instances of this class other than the static one
    }

    /**
     * Run a task in a separate thread as soon as possible
     *
     * @param runnable Task to run
     */
    public static void runTask(Runnable runnable) {
        EXECUTOR.submit(runnable);
    }

    /**
     * Run a task in a separate thread as soon as possible, yielding a Result in
     * the future
     *
     * @param <T> Result type of the task
     * @param runnable Task to run
     * @param type Type of object to return
     * @return Future object which will be filled at the completion of the Task.
     */
    public static <T> Future<T> submit(Runnable runnable, T type) {
        return EXECUTOR.submit(runnable, type);
    }

}
