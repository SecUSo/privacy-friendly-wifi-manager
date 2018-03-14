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

package org.secuso.privacyfriendlywifi.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;

import java.util.ArrayList;

/**
 * Service for scanning for Wi-Fis and updating MAC addresses.
 */
public class WifiUpdaterService extends Service {
    public static final String TAG = WifiUpdaterService.class.getSimpleName();


    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        int ret = super.onStartCommand(intent, flags, startId);

        Logger.d(TAG, "WifiUpdaterService invoked. Scanning for new MACs.");

        final WifiUpdaterService self = this;


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                // unregister this receiver
                try {
                    self.unregisterReceiver(this);
                } catch (IllegalArgumentException e) {
                    Logger.d(TAG, "Unregister failed: WiFiUpdaterService has not been registered previously.");
                }

                Logger.v(TAG, "Scan completed.");

                // fetch search results
                if (WifiHandler.scanAndUpdateWifis(context, new ArrayList<WifiLocationEntry>())) {
                    Logger.d(TAG, "Found new MACs.");
                    // AlarmReceiver.fire();
                } else {
                    Logger.d(TAG, "No new MACs.");
                }

                Logger.d(TAG, "Finishing.");


                // stop service
                stopForeground(false);
                stopSelf(startId);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        // register receiver as callback handler for WiFi scan
        this.registerReceiver(receiver, filter);

        // start actual scan
        WifiHandler.getWifiManager(this).startScan();

        return ret;
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
