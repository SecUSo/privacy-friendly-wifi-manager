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

public class ListHandler<EntryType extends PreconditionEntry> extends SerializationHelper implements Observer, IListHandler<EntryType> {
    private static final long serialVersionUID = 354678322122715718L;
    private final String TAG = ListHandler.class.getSimpleName();
    private static final int MAX_TRIES = 3;
    private List<EntryType> entries;
    private String listFilePath;

    public ListHandler(Context context, String listFilePath) {
        initialize(context, listFilePath);
    }

    public void initialize(Context context, String listFilePath) {
        initialize(context, listFilePath, 0);
    }

    @Override
    public void initialize(Context context, Object[] args) throws IOException {
        if (args.length > 0) {
            String listFilePath = (String) args[0];
            if (args.length > 1) {
                initialize(context, listFilePath, (int) args[1]);
            } else {
                initialize(context, listFilePath);
            }
        }
    }

    public void initialize(Context context, String listFilePath, int tries) {
        StaticContext.setContext(context);
        this.listFilePath = listFilePath;

        try {
            Object o = FileHandler.loadObject(context, this.listFilePath, false);

            //noinspection unchecked
            this.entries = (List<EntryType>) o;
        } catch (IOException e) {

            // try it multiple times (if e.g. file is only partially written by the service until now)
            if (tries < ListHandler.MAX_TRIES) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }

                initialize(context, listFilePath, tries + 1);
            } else {
                Logger.e(TAG, "Could not load file '" + this.listFilePath + "': " + e.getMessage());

                // File could not be loaded -> time to start from scratch
                this.entries = new ArrayList<>();
            }
        }
    }

    public boolean save() {
        return save(0);
    }

    public boolean save(int tries) {
        boolean saved;
        try {
            saved = FileHandler.storeObject(StaticContext.getContext(), this.listFilePath, this.entries);
        } catch (IOException e) {
            e.printStackTrace();
            saved = false;
        }

        if (!saved) {
            if (tries < ListHandler.MAX_TRIES) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }

                return save(tries + 1);
            }

            Logger.e(TAG, "Could not save file '" + this.listFilePath);
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
        // inspection not needed as indexOf takes care of this itself
        //noinspection SuspiciousMethodCalls
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

    @Override
    protected Object[] getPersistentFields() {
        return new Object[0];
    }
}
