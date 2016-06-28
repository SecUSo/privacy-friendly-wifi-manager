package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.types.PreconditionEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class ListHandler<EntryType extends PreconditionEntry> implements Observer, IListHandler<EntryType> {
    private final ListHandler.ListObservable listObservable = new ListHandler.ListObservable();
    private List<EntryType> entries;
    private final String listFilePath;

    private Context context;

    public ListHandler(Context context, String listFilePath) {
        this.context = context;
        this.listFilePath = listFilePath;

        try {
            Object o = FileHandler.loadObject(this.context, this.listFilePath, false);
            this.entries = (List<EntryType>) o;
        } catch (IOException e) {
            // File does not exist
            this.entries = new ArrayList<>();
        }
    }

    public void addObserver(Observer observer) {
        this.listObservable.addObserver(observer);
    }

    public boolean save() {
        try {
            FileHandler.storeObject(this.context, this.listFilePath, this.entries);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean add(EntryType newEntry) {
        boolean ret = this.entries.add(newEntry);
        this.save();
        this.listObservable.setChanged();
        this.listObservable.notifyObservers();
        return ret;
    }

    public boolean addAll(List<EntryType> newEntries) {
        boolean ret = this.entries.addAll(newEntries);
        this.save();
        this.listObservable.setChanged();
        this.listObservable.notifyObservers();
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
        this.save();
        this.listObservable.setChanged();
        this.listObservable.notifyObservers();
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

    @Override
    public void update(Observable observable, Object data) {
        this.save();
        this.listObservable.setChanged();
        this.listObservable.notifyObservers();
    }

    private class ListObservable extends Observable {
        public void setChanged() {
            super.setChanged();
        }
    }
}
