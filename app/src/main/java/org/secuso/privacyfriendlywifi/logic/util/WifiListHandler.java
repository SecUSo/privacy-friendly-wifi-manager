package org.secuso.privacyfriendlywifi.logic.util;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.util.List;
import java.util.Observer;

/**
 *
 */
public class WifiListHandler implements IListHandler<WifiLocationEntry> {
    private static ListHandler<WifiLocationEntry> list;

    public WifiListHandler() {
        if (WifiListHandler.list == null) {
            WifiListHandler.list = new ListHandler<>(ManagerService.FN_LOCATION_ENTRIES);
        }
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

    public void add(WifiLocationEntry newEntry) {
        WifiListHandler.list.add(newEntry);
    }

    public void addAll(List<WifiLocationEntry> newEntries) {
        WifiListHandler.list.addAll(newEntries);
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
}
