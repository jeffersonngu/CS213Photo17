package com.photos.utility;

import com.photos.PhotosApplication;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Wrapper for ArrayList that internally serializes data for operations. All modification
 * functions override their ArrayList equivalents to call {@link PhotosApplication#serializeData()}
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
        PhotosApplication.serializeData();
        return set;
    }

    @Override
    public boolean add(E e) {
        boolean add = super.add(e);
        PhotosApplication.serializeData();
        return add;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        PhotosApplication.serializeData();
    }

    @Override
    public E remove(int index) {
        E remove = super.remove(index);
        PhotosApplication.serializeData();
        return remove;
    }

    @Override
    public boolean remove(Object o) {
        boolean remove = super.remove(o);
        PhotosApplication.serializeData();
        return remove;
    }

    @Override
    public void clear() {
        super.clear();
        PhotosApplication.serializeData();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean addAll = super.addAll(c);
        PhotosApplication.serializeData();
        return addAll;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean addAll = super.addAll(index, c);
        PhotosApplication.serializeData();
        return addAll;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removeAll = super.removeAll(c);
        PhotosApplication.serializeData();
        return removeAll;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean retainAll = super.retainAll(c);
        PhotosApplication.serializeData();
        return retainAll;
    }
}
