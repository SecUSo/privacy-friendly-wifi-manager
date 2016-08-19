package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.types.PreconditionEntry;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class ListHandler<EntryType extends PreconditionEntry> extends Observable implements Observer, IListHandler<EntryType> {
    private List<EntryType> entries;
    private String listFilePath;

    public ListHandler(Context context, String listFilePath) {
        initialize(context, listFilePath);
    }

    public void initialize(Context context, String listFilePath) {
        StaticContext.setContext(context);
        this.listFilePath = listFilePath;

        try {
            Object o = FileHandler.loadObject(context, this.listFilePath, false);
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
        if (!this.entries.contains(newEntry)) {
            newEntry.addObserver(this);
            this.entries.add(newEntry);
            sort();

            return true;
        }

        return false;
    }

    public boolean addAll(List<EntryType> newEntries) {
        HashSet<EntryType> toAdd = new HashSet<>(this.entries);

        toAdd.retainAll(newEntries);

        if (toAdd.size() > 0) {

            for (EntryType entry : toAdd) {
                entry.addObserver(this);
            }

            this.entries.addAll(toAdd);
            sort();

            return true;
        }

        return false;
    }

    public void sort() {
        Collections.sort(this.entries);
        this.notifyChanged();
    }

    public List<EntryType> getAll() {
        return this.entries; // don't return copy of the list
    }

    public EntryType get(int location) {
        return this.entries.get(location);
    }

    public boolean remove(EntryType entry) {
        if (this.entries.remove(entry)) {
            this.notifyChanged();
            return true;
        }

        return false;
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
        AlarmReceiver.fireAndSchedule(StaticContext.getContext());
        this.notifyChanged();
    }
}
