package com.test.alternateitr;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class TestIterator {
    
  
    List<String> stringList;
    
    @Test
    public void hasNextShouldReturnTrueIfCollectionHasNextValue() {
        stringList = new ArrayList<>(Arrays.asList("A","B","C","D"));
        EverySecondIterator<String> itr = new EverySecondIterator<>(stringList.iterator());
        Assert.assertTrue(itr.hasNext());
        
    }
    
    @Test
    public void hasNextShouldReturnFalseIfCollectionDoesNotHaveNextValue() {
        stringList = new ArrayList<>(Arrays.asList("A","B"));
        EverySecondIterator<String> itr = new EverySecondIterator<>(stringList.iterator());
        Assert.assertTrue(itr.hasNext());
        itr.next();
        Assert.assertFalse(itr.hasNext());
    }
    
    @Test
    public void nextShouldResturnNextValueIfCollectionHasNextElement() {
        stringList = new ArrayList<>(Arrays.asList("A","B"));
        EverySecondIterator<String> itr = new EverySecondIterator<>(stringList.iterator());
        Assert.assertTrue(itr.hasNext());
        Assert.assertTrue(itr.next().equals("B"));
        
    }
    @Test
    public void nextShouldThrowExceptionIfThereIsNoNextElementInTheCollection() {
        stringList = new ArrayList<>(Arrays.asList("A","B"));
        EverySecondIterator<String> itr = new EverySecondIterator<>(stringList.iterator());
        Assert.assertTrue(itr.hasNext());
        itr.next();
        Assert.assertFalse(itr.hasNext());
        itr.next();
        
    }

}
