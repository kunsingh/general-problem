package com.test.alternateitr;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class EverySecondIterator<T> implements Iterator<T> {

    private final Iterator<T> originalItr;
    
    public EverySecondIterator(final Iterator<T> originalItr) {
        this.originalItr = originalItr;
    }

    private boolean skipped;
    
    @Override
    public boolean hasNext() {
        //skip one elemnet
        
        if(!skipped) {
            if(originalItr.hasNext()) {
                originalItr.next();
                skipped = true;
            }
        }
        
        return originalItr.hasNext();
    }

    @Override
    public T next() {
        if(hasNext()) {
            skipped = false;
            return originalItr.next();
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        
    }


}
