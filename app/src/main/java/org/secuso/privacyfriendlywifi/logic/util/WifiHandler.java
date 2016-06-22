package org.secuso.privacyfriendlywifi.logic.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.ContextCompat;

/**
 *
 */
public class WifiHandler {

    public static boolean hasWifiPermission(Context context) {
        return (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CHANGE_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean isWifiConnected(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            int nid = wifiInfo.getNetworkId();
            return wifiManager.isWifiEnabled() && nid != -1;
        }

        return false;
    }

    public static String getCleanSSID(String rawSSID) {
        String cleanSSID = rawSSID;
        if (cleanSSID.startsWith("\"") && cleanSSID.endsWith("\"")) {
            cleanSSID = cleanSSID.substring(1, cleanSSID.length() - 1);
        }
        return cleanSSID;
    }
}
