package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

/**
 *
 */
public interface IListHandler<EntryType> {

    void initialize(Context context, Object[] args) throws IOException;

    void addObserver(Observer observer);

    boolean save();

    boolean add(EntryType newEntry);

    boolean addAll(List<EntryType> newEntries);

    void sort();

    List<EntryType> getAll();

    EntryType get(int location);

    boolean remove(EntryType entry);

    int size();

    int indexOf(Object o);

    boolean isEmpty();
}
