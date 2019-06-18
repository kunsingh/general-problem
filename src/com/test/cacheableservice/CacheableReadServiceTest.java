package com.test.cacheableservice;

import java.util.Random;

import org.junit.Test;

import junit.framework.Assert;

public class CacheableReadServiceTest {
   
    private ReadService<String> readService = new ReadService<String>() {
        
        @Override
        public String read(int id) {
            return "Test";
        }
    };
    
    @Test
    public void shouldReadTheValueIfPresent() {
        ReadServiceCacheable<String> readServiceCacheable = new ReadServiceCacheable<>(readService);
        Assert.assertTrue(readServiceCacheable.read(1) != null);
        
        
    }
    @Test
    public void shouldCacheTheValueIfNotPresent() {
        
    }
    
    @Test
    public void ShouldThrowExceptionIfSourceServiceReadTHrowException() {
        
    }
    

}
