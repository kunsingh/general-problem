package com.test.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadExecutor<T> {

    private static AtomicInteger ID = new AtomicInteger(0);
    private final int id;
    private ExecutorService executorService;
    private ExecutorCompletionService<T> completionService;
    private AtomicInteger pendingTasks;

    public ThreadExecutor(){
        this.id = ID.incrementAndGet();
        this.executorService =  Executors.newSingleThreadExecutor();
        completionService = new ExecutorCompletionService<T>(executorService);
        pendingTasks = new AtomicInteger(0);
    }

    public FutureHandler<T> submit(T obj, boolean isReturnFuture){
        Future<T> future = completionService.submit(new InnerTaskExecutor(obj, this.toString()));
        return isReturnFuture? new FutureHandler<T>(future, obj):null;
    }

    public Future<T> getCompletedTaskFuture(){
        return completionService.poll();
    }

    private class InnerTaskExecutor implements Callable<T> {

        private final T obj;
        private final String name;


        public InnerTaskExecutor(T obj, String name) {
            this.obj = obj;
            this.name = name;
        }

        @Override
        public T call() throws Exception {
            System.out.println("Handling message :" + obj +" By: "+ name);
            Thread.sleep(1000);
            System.out.println("Completed message :" + obj +" By: "+ name);
            return obj;
        }
    }

    @Override
    public String toString() {
        return "Executor: "+id;
    }
}
