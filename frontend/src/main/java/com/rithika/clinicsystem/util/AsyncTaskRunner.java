package com.rithika.clinicsystem.util;

import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class AsyncTaskRunner {

    private static final AtomicInteger THREAD_COUNTER =
            new AtomicInteger(1);

    private static final ExecutorService EXECUTOR =
            Executors.newFixedThreadPool(
                    4,
                    runnable -> {

                        Thread thread =
                                new Thread(
                                        runnable,
                                        "carenexus-api-"
                                                + THREAD_COUNTER.getAndIncrement()
                                );

                        thread.setDaemon(true);

                        return thread;
                    }
            );

    private AsyncTaskRunner() {
    }

    public static <T> void run(
            Supplier<T> operation,
            Consumer<T> onSuccess,
            Consumer<Throwable> onFailure
    ) {

        Task<T> task =
                new Task<>() {

                    @Override
                    protected T call() {
                        return operation.get();
                    }
                };

        task.setOnSucceeded(
                event ->
                        onSuccess.accept(
                                task.getValue()
                        )
        );

        task.setOnFailed(
                event ->
                        onFailure.accept(
                                task.getException()
                        )
        );

        EXECUTOR.submit(task);
    }

    public static void run(
            Runnable operation,
            Runnable onSuccess,
            Consumer<Throwable> onFailure
    ) {

        run(
                () -> {
                    operation.run();
                    return null;
                },
                ignored -> onSuccess.run(),
                onFailure
        );
    }
}