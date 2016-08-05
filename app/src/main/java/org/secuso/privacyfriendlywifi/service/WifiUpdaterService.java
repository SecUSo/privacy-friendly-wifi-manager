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

        this.registerReceiver(receiver, filter);

        WifiHandler.getWifiManager(this).startScan();

        return ret;
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
