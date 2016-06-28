package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 */
public class WifiListHandler {
    public static final Observable listObservable = new WifiListHandler.ListObservable();
    private static List<WifiLocationEntry> wifiLocationEntries;

    protected static void initWifiLocationEntries(Context context) {
        if (WifiListHandler.wifiLocationEntries == null) {
            try {
                Object o = FileHandler.loadObject(context, ManagerService.FN_LOCATION_ENTRIES, false);
                WifiListHandler.wifiLocationEntries = (List<WifiLocationEntry>) o;
            } catch (IOException e) {
                // File does not exist
                WifiListHandler.wifiLocationEntries = new ArrayList<>();
            }
        }
    }

    protected static boolean saveWifiLocationEntries(Context context) {
        try {
            FileHandler.storeObject(context, ManagerService.FN_LOCATION_ENTRIES, WifiListHandler.wifiLocationEntries);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public List<WifiLocationEntry> getWifiLocationEntries(Context context) {
        WifiListHandler.initWifiLocationEntries(context);
        return WifiListHandler.wifiLocationEntries; // don't return copy of the list
    }

    public static boolean addWifiLocationEntry(Context context, WifiLocationEntry newEntry) {
        WifiListHandler.initWifiLocationEntries(context);
        boolean ret = WifiListHandler.wifiLocationEntries.add(newEntry);
        WifiListHandler.saveWifiLocationEntries(context);
        return ret;
    }

    public static boolean addAllWifiLocationEntry(Context context, List<WifiLocationEntry> newEntries) {
        WifiListHandler.initWifiLocationEntries(context);
        boolean ret = WifiListHandler.wifiLocationEntries.addAll(newEntries);
        WifiListHandler.saveWifiLocationEntries(context);
        WifiListHandler.listObservable.notifyObservers();
        return ret;
    }

    public static boolean removeWifiLocationEntry(Context context, WifiLocationEntry entry) {
        //TODO implement
        return false;
    }

    private static class ListObservable extends Observable {
    }
}
