package org.secuso.privacyfriendlywifi.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.service.ManagerService;
import org.secuso.privacyfriendlywifi.service.NewWifiNotification;

/**
 * BroadcastReceiver to listen for Wi-Fi state changes.
 * If connected to a new wireless network, a notification will be shown.
 */
public class WifiChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ManagerService.isServiceRunning(context)) { // check that the app is actually expected to manage wifi

            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

            if (info != null && info.isConnected()) { // wifi is connected

                // get current ssid
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();

                if (ssid.equals("<unknown ssid>") || ssid.equals("0x")) { // check if connected correctly
                    return;
                }

                boolean found = false;
                for (WifiLocationEntry entry : ManagerService.getWifiLocationEntries(context)) {
                    if (entry.getSsid().equals(ssid)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    NewWifiNotification.show(context, ssid);
                }
            }
        }
    }
}
