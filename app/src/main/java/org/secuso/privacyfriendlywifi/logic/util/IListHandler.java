package org.secuso.privacyfriendlywifi.logic.util;

import java.util.List;
import java.util.Observer;

/**
 * Interface for list handlers
 */
public interface IListHandler<EntryType> {

    /**
     * Registers an observer to the list handler.
     *
     * @param observer Observer to register.
     */
    void addObserver(Observer observer);

    /**
     * Saves the underlying list.
     *
     * @return True, if file could be saved.
     */
    boolean save();

    /**
     * Adds an element to the list.
     *
     * @param newEntry The entry to add.
     * @return True, if the entry has been added (i.e. the list did not contain the entry already)
     */
    boolean add(EntryType newEntry);

    /**
     * Adds multiple elements to the list.
     *
     * @param newEntries The entries to add.
     * @return True, if the entries have been added (i.e. the list did not contain the entry already)
     */
    boolean addAll(List<EntryType> newEntries);

    /**
     * Sorts the underlying list using the respective comparator.
     */
    void sort();

    /**
     * Returns the underlying list (a direct pointer, no shallow copy).
     *
     * @return The list of elements.
     */
    List<EntryType> getAll();

    /**
     * Returns the entry at the given position.
     *
     * @param position Position in list.
     * @return The element at the given position. If the index is out of bounds, an IndexOutOfBounds exception is thrown.
     */
    EntryType get(int position);

    /**
     * Removes the given entry from the list.
     *
     * @param entry The entry to remove.
     * @return True, if the entry has been removed.
     */
    boolean remove(EntryType entry);

    /**
     * Returns the size of the list.
     *
     * @return the size of the list.
     */
    int size();

    /**
     * Returns the index of the given object
     *
     * @param o An object to find.
     * @return The index of the given object. -1 if it is not part of the list.
     */
    int indexOf(Object o);

    /**
     * Returns true if the list is empty, false otherwise.
     *
     * @return true if the list is empty, false otherwise.
     */
    boolean isEmpty();
}
