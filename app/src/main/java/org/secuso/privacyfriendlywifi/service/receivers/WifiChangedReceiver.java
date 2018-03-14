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

package org.secuso.privacyfriendlywifi.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;
import org.secuso.privacyfriendlywifi.logic.util.WifiListHandler;
import org.secuso.privacyfriendlywifi.service.WifiNotification;

/**
 * BroadcastReceiver to listen for Wi-Fi state changes.
 * If connected to a new wireless network, a notification will be shown.
 */
public class WifiChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        StaticContext.setContext(context);
        WifiListHandler wifiListHandler = new WifiListHandler(context);
        wifiListHandler.sort(); // sort list in order to reflect state changes is Wi-Fi list fragment

        if (SettingsEntry.isServiceActive(context)) { // check that the app is actually expected to manage wifi
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

            if (info != null && info.isConnected()) { // wifi is connected

                // get current ssid
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = WifiHandler.getCleanSSID(wifiInfo.getSSID());

                if (ssid.equals("<unknown ssid>") || ssid.equals("0x")) { // check if connected correctly
                    return;
                }


                // check whether the wifi is already known
                for (WifiLocationEntry entry : wifiListHandler.getAll()) {
                    if (entry.getSsid().equals(ssid)) {
                        return;
                    }
                }

                // if unknown, show notification - check settings before

                SharedPreferences settings = context.getSharedPreferences(SettingsEntry.PREF_SETTINGS, Context.MODE_PRIVATE);
                if (settings.getBoolean(SettingsEntry.PREF_ENTRY_SHOW_NOTIFICATION, true)) {
                    WifiNotification.show(context, wifiInfo);
                }

            } else {
                AlarmReceiver.fireAndSchedule(context, false); // not connected -> maybe we can already disable Wi-Fi
            }
        }
    }
}
