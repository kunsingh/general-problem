package com.test.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskDelegator {

    private static final ConcurrentMap<String, ThreadExecutor<String>> pfAndCusipExecutor = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, ThreadExecutor<String>> pfExecutor = new ConcurrentHashMap<>();
    private static final Semaphore semaphore = new Semaphore(1);
    private static final AtomicInteger pfAndCusipTaskCount = new AtomicInteger(0);
    private static final AtomicInteger pfTaskCount = new AtomicInteger(0);

    static {
        start();
    }

    public static void start() {

        new Thread(() -> {
            while (true) {
                for (Map.Entry<String, ThreadExecutor<String>> entry : pfAndCusipExecutor.entrySet()) {
                    ThreadExecutor executor = entry.getValue();

                    while (executor.getCompletedTaskFuture() != null) {
                        System.out.println("non-Global task count:: " + pfAndCusipTaskCount.decrementAndGet());
                        if(pfAndCusipTaskCount.get() <= 0){
                            MessageLocks.acquireLock("message");
                            MessageLocks.signalAllNonGlobal();
                            MessageLocks.releaseLock("message");
                        }

                    }
                }
            }

        }).start();
        new Thread(() -> {
            while (true) {
                for (Map.Entry<String, ThreadExecutor<String>> entry : pfExecutor.entrySet()) {
                    ThreadExecutor executor = entry.getValue();

                    while (executor.getCompletedTaskFuture() != null) {

                        System.out.println(" Global task count:: " + pfTaskCount.decrementAndGet());
                        if(pfTaskCount.get() <= 0){
                            MessageLocks.acquireLock("message");
                            MessageLocks.signalAllGlobal();
                            MessageLocks.releaseLock("message");
                        }

                    }
                }
            }

        }).start();
    }

    public static void submitTask(String message, String type) {
        MessageLocks.acquireLock(message);
        System.out.println("Received: "+message +" Type: "+type);
        if(type.equals("GLOBAL")){

            while(pfAndCusipTaskCount.get() > 0) {
                System.out.println("pfAndCusipTaskCount.get(): "+pfAndCusipTaskCount.get());
                MessageLocks.waitForNonGlobalMessageToFinish();
            }
        }
        while(pfTaskCount.get() > 0) {
            System.out.println("pfTaskCount.get(): "+pfTaskCount.get());
            MessageLocks.waitForGlobalMessageToFinish();
        }
        ThreadExecutor<String> executor;

        if(type.equals("GLOBAL")) {
            executor = getOrCreateExecutor(message,  type);
            pfTaskCount.incrementAndGet();
        }else{
            executor = getOrCreateExecutor(message,  type);
            pfAndCusipTaskCount.incrementAndGet();
        }
        FutureHandler<String> futureHandler = executor.submit(message, true);
        MessageLocks.releaseLock(message);
    }

    private static ThreadExecutor<String> getOrCreateExecutor(String message, String type) {
        ThreadExecutor<String> executor = null;
        if(type.equals("GLOBAL")) {
            if (!pfExecutor.containsKey(message)) {
                System.out.println("Creating Executor for GLOBAL: " + message);
            }
            pfExecutor.putIfAbsent(message, new ThreadExecutor<>());
            executor = pfExecutor.get(message);

        }else{
            if (!pfAndCusipExecutor.containsKey(message)) {
                System.out.println("Creating Executor for Non-GLOBAL: " + message);
            }
            pfAndCusipExecutor.putIfAbsent(message, new ThreadExecutor<>());
            executor = pfAndCusipExecutor.get(message);
        }
         return executor;
    }
}
