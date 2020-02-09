package com.test.thread;

import java.util.concurrent.Future;

public class FutureHandler<T> {

    private final Future<T> future;
    private final T message;

    public FutureHandler(Future<T> future, T message){
        this.future = future;
        this.message = message;
    }

    public Future<T> getFuture() {
        return future;
    }

    public T getMessage() {
        return message;
    }
}
