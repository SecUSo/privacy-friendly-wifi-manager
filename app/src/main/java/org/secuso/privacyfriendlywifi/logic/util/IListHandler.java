package org.secuso.privacyfriendlywifi.logic.util;

import java.util.List;

/**
 *
 */
public interface IListHandler<EntryType> {


    boolean save();

    boolean add(EntryType newEntry);

    boolean addAll(List<EntryType> newEntries);

    List<EntryType> getAll();

}
