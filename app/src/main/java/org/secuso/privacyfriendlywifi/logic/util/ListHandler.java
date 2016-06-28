package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 */
public class ListHandler<EntryType> implements IListHandler<EntryType> {
    public final Observable listObservable = new ListHandler.ListObservable();
    private List<EntryType> entries;

    private Context context;

    public ListHandler(Context context) {
        this.context = context;

        try {
            Object o = FileHandler.loadObject(this.context, ManagerService.FN_LOCATION_ENTRIES, false);
            this.entries = (List<EntryType>) o;
        } catch (IOException e) {
            // File does not exist
            this.entries = new ArrayList<>();
        }
    }

    public boolean save() {
        try {
            FileHandler.storeObject(this.context, ManagerService.FN_LOCATION_ENTRIES, this.entries);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean add(EntryType newEntry) {
        boolean ret = this.entries.add(newEntry);
        this.save();
        this.listObservable.notifyObservers();
        return ret;
    }

    public boolean addAll(List<EntryType> newEntries) {
        boolean ret = this.entries.addAll(newEntries);
        this.save();
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

    private class ListObservable extends Observable {
    }
}
