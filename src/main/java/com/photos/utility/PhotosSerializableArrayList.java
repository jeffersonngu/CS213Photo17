package com.photos.utility;

import com.photos.Photos;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Wrapper for ArrayList that internally serializes data for operations. All modification
 * functions override their ArrayList equivalents to call {@link Photos#serializeData()}
 * @param <E>
 */
public class PhotosSerializableArrayList<E> extends ArrayList<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public PhotosSerializableArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public PhotosSerializableArrayList() {
        super();
    }

    public PhotosSerializableArrayList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public E set(int index, E element) {
        E set = super.set(index, element);
        Photos.serializeData();
        return set;
    }

    @Override
    public boolean add(E e) {
        boolean add = super.add(e);
        Photos.serializeData();
        return add;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        Photos.serializeData();
    }

    @Override
    public E remove(int index) {
        E remove = super.remove(index);
        Photos.serializeData();
        return remove;
    }

    @Override
    public boolean remove(Object o) {
        boolean remove = super.remove(o);
        Photos.serializeData();
        return remove;
    }

    @Override
    public void clear() {
        super.clear();
        Photos.serializeData();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean addAll = super.addAll(c);
        Photos.serializeData();
        return addAll;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean addAll = super.addAll(index, c);
        Photos.serializeData();
        return addAll;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removeAll = super.removeAll(c);
        Photos.serializeData();
        return removeAll;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean retainAll = super.retainAll(c);
        Photos.serializeData();
        return retainAll;
    }
}
