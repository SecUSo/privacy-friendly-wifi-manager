package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;

import java.util.List;

/**
 *
 */
public class WifiListHandler implements IListHandler<WifiLocationEntry> {
    static ListHandler<WifiLocationEntry> list;

    public WifiListHandler(Context context) {
        if (list == null) {
            list = new ListHandler<>(context);
        }
    }

    public boolean save() {
        return list.save();
    }

    public List<WifiLocationEntry> getAll() {
        return list.getAll();
    }

    public boolean add(WifiLocationEntry newEntry) {
        return list.add(newEntry);
    }

    public boolean addAll(List<WifiLocationEntry> newEntries) {
        return list.addAll(newEntries);
    }

    public boolean remove(WifiLocationEntry entry) {
        return list.remove(entry);
    }
}
