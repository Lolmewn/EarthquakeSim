package nl.lolmewn.rug.quakecommon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Lolmewn
 */
public class Threader {
    
    private final static ExecutorService EXECUTOR;
    
    static{
        EXECUTOR = Executors.newCachedThreadPool();
    }

    private Threader() {
        // Don't allow instances of this class other than the static one
    }
    
    public static void runTask(Runnable runnable){
        EXECUTOR.submit(runnable);
    }

    public static <T> Future<T> submit(Runnable r, T t) {
        return EXECUTOR.submit(r, t);
    }

}
