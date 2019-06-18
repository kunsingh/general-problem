package com.test.cacheableservice;

import java.util.concurrent.ConcurrentHashMap;

public class ReadServiceCacheable<T> implements ReadService<T> {

    private final ConcurrentHashMap<Integer, T> map = new ConcurrentHashMap<>();

    private final ReadService<T> sourceReadService;

    public ReadServiceCacheable(ReadService<T> sourceReadService) {
        this.sourceReadService = sourceReadService;
    }

    @Override
    public T read(int id) {
        map.putIfAbsent(id, sourceReadService.read(id));
        return map.get(id);
    }

    public static <T> ReadService<T> wrapWithCache(final ReadService<T> service) {
        return new ReadServiceCacheable<>(service);
    }

}
