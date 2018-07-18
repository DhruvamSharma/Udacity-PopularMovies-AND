package com.dhruvam.popularmovies.executor;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.androidnetworking.core.MainThreadExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A global class for app executors that act as a layer above different tasks like
 * DiskIO, NetworkIO or mainThread. Provides a thread safe environment
 * for different operations.
 */
public class AppExecutor {

    private static final Object LOCK = new Object();
    private static AppExecutor sExecutor;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    private AppExecutor(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static AppExecutor getInstance() {
        if (sExecutor == null)
        synchronized (LOCK) {
            sExecutor = new AppExecutor(Executors.newSingleThreadExecutor(),
                                        Executors.newFixedThreadPool(3),
                                        new MainThreadExecutor());
        }
        return sExecutor;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
