package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

/**
 *
 */
public class WifiListHandler extends SerializationHelper implements IListHandler<WifiLocationEntry> {
    private static ListHandler<WifiLocationEntry> list;

    public WifiListHandler(Context context) {
        if (list == null) {
            WifiListHandler.list = new ListHandler<WifiLocationEntry>(context, ManagerService.FN_LOCATION_ENTRIES);
        }
    }

    public void initialize(Context context, Object[] args) throws IOException {
        if (args[0] instanceof ListHandler) {
            WifiListHandler.list = (ListHandler<WifiLocationEntry>) args[0];
        } else {
            throw new IOException(SerializationHelper.SERIALIZATION_ERROR);
        }
    }

    @Override
    protected Object[] getPersistentFields() {
        return new Object[]{list};
    }

    public void addObserver(Observer observer) {
        WifiListHandler.list.addObserver(observer);
    }

    public boolean save() {
        return WifiListHandler.list.save();
    }

    public List<WifiLocationEntry> getAll() {
        return WifiListHandler.list.getAll();
    }

    public WifiLocationEntry get(int location) {
        return WifiListHandler.list.get(location);
    }

    public boolean add(WifiLocationEntry newEntry) {
        return WifiListHandler.list.add(newEntry);
    }

    public boolean addAll(List<WifiLocationEntry> newEntries) {
        return WifiListHandler.list.addAll(newEntries);
    }

    public void sort() {
        WifiListHandler.list.sort();
    }

    public boolean remove(WifiLocationEntry entry) {
        return WifiListHandler.list.remove(entry);
    }

    public int size() {
        return WifiListHandler.list.size();
    }

    public int indexOf(Object o) {
        return WifiListHandler.list.indexOf(o);
    }

    public boolean isEmpty() {
        return WifiListHandler.list.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<WifiLocationEntry> entries = this.getAll();

        sb.append("### Wi-Fi List ###\n");
        for (int i = 0; i < entries.size(); i++) {
            sb.append("[").append(entries.get(i).toString()).append("]\n");
        }

        return sb.toString();
    }
}
