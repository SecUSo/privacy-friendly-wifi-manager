package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;

import java.util.List;

/**
 *
 */
public class WifiListHandler extends ListHandler<WifiLocationEntry> {
    private static ListHandler<WifiLocationEntry> handler = new ListHandler<WifiLocationEntry>() {
        @Override
        public boolean addWifiLocationEntry(Context context, WifiLocationEntry newEntry) {
            return super.addWifiLocationEntry(context, newEntry);
        }

        @Override
        public boolean addAllWifiLocationEntry(Context context, List<WifiLocationEntry> newEntries) {
            return super.addAllWifiLocationEntry(context, newEntries);
        }

        @Override
        public List<WifiLocationEntry> getWifiLocationEntries(Context context) {
            return super.getWifiLocationEntries(context);
        }
    };

    public static boolean addWifiLocationEntry(Context context, WifiLocationEntry newEntry) {
        return WifiListHandler.handler.addWifiLocationEntry(context, newEntry);
    }

    public boolean addAllWifiLocationEntry(Context context, List<WifiLocationEntry> newEntries) {
    }

    public List<WifiLocationEntry> getWifiLocationEntries(Context context) {

    }
}
