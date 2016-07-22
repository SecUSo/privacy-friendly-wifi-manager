package org.secuso.privacyfriendlywifi.logic.util;

import org.secuso.privacyfriendlywifi.logic.types.PreconditionEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class ListHandler<EntryType extends PreconditionEntry> extends Observable implements Observer, IListHandler<EntryType> {
    private List<EntryType> entries;
    private final String listFilePath;

    public ListHandler(String listFilePath) {
        this.listFilePath = listFilePath;

        try {
            Object o = FileHandler.loadObject(StaticContext.getContext(), this.listFilePath, false);
            this.entries = (List<EntryType>) o;
        } catch (IOException e) {
            // File does not exist
            this.entries = new ArrayList<>();
        }
    }

    public boolean save() {
        try {
            FileHandler.storeObject(StaticContext.getContext(), this.listFilePath, this.entries);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean add(EntryType newEntry) {
        newEntry.addObserver(this);
        boolean ret = this.entries.add(newEntry);

        this.notifyChanged();

        return ret;
    }

    public boolean addAll(List<EntryType> newEntries) {
        for (EntryType entry : newEntries) {
            entry.addObserver(this);
        }

        boolean ret = this.entries.addAll(newEntries);

        this.notifyChanged();

        return ret;
    }

    public List<EntryType> getAll() {
        return this.entries; // don't return copy of the list
    }

    public EntryType get(int location) {
        return this.entries.get(location);
    }

    public boolean remove(EntryType entry) {
        boolean ret = this.entries.remove(entry);

        this.notifyChanged();

        return ret;
    }

    public int size() {
        return this.entries.size();
    }

    public int indexOf(Object o) {
        return this.entries.indexOf(o);
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    private void notifyChanged() {
        this.save();
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public void update(Observable observable, Object data) {
        this.notifyChanged();
    }
}
