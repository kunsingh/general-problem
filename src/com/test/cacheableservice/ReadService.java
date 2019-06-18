package com.test.cacheableservice;

public interface ReadService<T> {

    T read(final int id);
}
