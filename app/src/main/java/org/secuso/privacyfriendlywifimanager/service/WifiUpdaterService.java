/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.secuso.privacyfriendlywifimanager.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.Logger;
import org.secuso.privacyfriendlywifimanager.logic.util.WifiHandler;

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
