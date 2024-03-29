package com.gufli.bookshelf.api.scheduler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface Scheduler {

    Executor sync();

    Executor async();

    //

//    default void async(Runnable task) {
//        async().execute(task);
//    }
//
//    default void sync(Runnable task) {
//        sync().execute(task);
//    }

    //

    SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit);

    SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit);

    //

    SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit);

    SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit);

    //

    default <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        async().execute(() -> {
            try {
                future.complete(supplier.get());
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    default CompletableFuture<Void> runAsync(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        async().execute(() -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

}
