package org.secuso.privacyfriendlywifi.logic.util;

import java.util.List;
import java.util.Observer;

/**
 *
 */
public interface IListHandler<EntryType> {

    void addObserver(Observer observer);

    boolean save();

    boolean add(EntryType newEntry);

    boolean addAll(List<EntryType> newEntries);

    List<EntryType> getAll();

    EntryType get(int location);

    boolean remove(EntryType entry);

    int size();

    int indexOf(Object o);

    boolean isEmpty();
}
