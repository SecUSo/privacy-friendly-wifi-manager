package org.secuso.privacyfriendlywifi.logic.util;

import java.util.List;
import java.util.Observable;

/**
 *
 */
public interface IListHandler<EntryType> {

    Observable getListObservable();

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
