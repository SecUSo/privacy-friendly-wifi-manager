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
public abstract class ListHandler<EntryType> {
    public final Observable listObservable = new ListHandler.ListObservable();
    private List<EntryType> wifiLocationEntries;

    protected void initWifiLocationEntries(Context context) {
        if (this.wifiLocationEntries == null) {
            try {
                Object o = FileHandler.loadObject(context, ManagerService.FN_LOCATION_ENTRIES, false);
                this.wifiLocationEntries = (List<EntryType>) o;
            } catch (IOException e) {
                // File does not exist
                this.wifiLocationEntries = new ArrayList<>();
            }
        }
    }

    protected boolean saveWifiLocationEntries(Context context) {
        try {
            FileHandler.storeObject(context, ManagerService.FN_LOCATION_ENTRIES, this.wifiLocationEntries);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addWifiLocationEntry(Context context, EntryType newEntry) {
        this.initWifiLocationEntries(context);
        boolean ret = this.wifiLocationEntries.add(newEntry);
        this.saveWifiLocationEntries(context);
        return ret;
    }

    public boolean addAllWifiLocationEntry(Context context, List<EntryType> newEntries) {
        this.initWifiLocationEntries(context);
        boolean ret = this.wifiLocationEntries.addAll(newEntries);
        this.saveWifiLocationEntries(context);
        this.listObservable.notifyObservers();
        return ret;
    }

    public List<EntryType> getWifiLocationEntries(Context context) {
        this.initWifiLocationEntries(context);
        return this.wifiLocationEntries; // don't return copy of the list
    }

    private static class ListObservable extends Observable {
    }
}
