package dev.minealert.utils;

import java.io.Serializable;
import java.util.Iterator;

public class WeakList<E> implements Serializable {

    private final Object[] arrayStorage;
    private int size;
    private final int capacity;
    public static final int DEFAULT_CAPACITY = 2500;


    public WeakList(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.arrayStorage = new Object[capacity];
    }

    public WeakList(int capacity, E... elements) {
        this.capacity = capacity;
        this.size = 0;
        this.arrayStorage = new Object[capacity];
        for (E element : elements) {
            add(element);
            //Prevents index out of bounds error
            if (getSize() >= capacity) {
                return;
            }
        }
    }

    public WeakList(E... elements) {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        this.arrayStorage = new Object[capacity];
        for (E element : elements) {
            add(element);
            //Prevents index out of bounds error
            if (getSize() >= capacity) {
                return;
            }
        }
    }

    public void add(E element) {
        arrayStorage[size++] = element;
    }

    public void remove(E element) {
        arrayStorage[size--] = element;
    }

    @SuppressWarnings("unchecked")
    public E getElement(int index) {
        return (E) arrayStorage[index];
    }

    @SuppressWarnings("unchecked")
    public E[] getElements() {
        return (E[]) arrayStorage;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Iterator<E> iterator() {
        return new WeakListIterator<>();
    }

    public int getCapacity() {
        if (capacity == 0) throw new RuntimeException("Capacity must be 1 or greater");
        return capacity;
    }

    private class WeakListIterator<E> implements Iterator<E> {

        private int current = 0;

        @Override
        public boolean hasNext() {
            return this.current < getSize();
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            return (E) arrayStorage[current++];
        }
    }
}