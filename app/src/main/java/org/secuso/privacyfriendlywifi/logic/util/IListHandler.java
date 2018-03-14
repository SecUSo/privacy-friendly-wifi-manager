/*
 This file is part of Privacy Friendly Wifi Manager.
 Privacy Friendly Wifi Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Wifi Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Wifi Manager. If not, see <http://www.gnu.org/licenses/>.
 */

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
