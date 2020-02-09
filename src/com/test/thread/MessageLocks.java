package com.test.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageLocks {

    private static final Map<String, Lock> messageToLockMap = new ConcurrentHashMap<>();
    private static final Lock lock = new ReentrantLock(true);
    private static final Condition waitForGlobalMessagesToFinish = lock.newCondition();
    private static final Condition waitForNonGlobalMessagesToFinish = lock.newCondition();

    public static void acquireLock(String message){
//        Lock lock = null;
//        String newMessage = message.intern();
//        synchronized (newMessage){
//            lock = messageToLockMap.get(newMessage);
//            if(null == lock){
//                lock = new ReentrantLock(true);
//                messageToLockMap.put(newMessage, lock);
//            }
//        }
        try{
            while (!lock.tryLock(100, TimeUnit.MICROSECONDS)){}
//            messageToLockMap.put(newMessage, lock);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

    public static void releaseLock(String message){
//        Lock lock = messageToLockMap.remove(message);
        lock.unlock();
    }

    public static void waitForNonGlobalMessageToFinish(){
        try {
            System.out.println("Waiting non global to finish...");
            waitForNonGlobalMessagesToFinish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void waitForGlobalMessageToFinish(){
        try {
            System.out.println("Waiting global to finish...");
            waitForGlobalMessagesToFinish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void signalAllNonGlobal(){
        System.out.println("Invoking all non-global...");
        waitForNonGlobalMessagesToFinish.signalAll();
    }

    public static void signalAllGlobal(){
        System.out.println("Invoking all non-global...");
        waitForGlobalMessagesToFinish.signalAll();
    }
}
