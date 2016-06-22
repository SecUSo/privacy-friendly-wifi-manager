package org.secuso.privacyfriendlywifi.logic.effects;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;

/**
 * (De-) Activate WiFi.
 */
public class WifiToggleEffect implements IEffect {
    static final String TAG = "WifiToggleEffect";
    Context context;

    public WifiToggleEffect(Context context) {
        this.context = context;
    }

    @Override
    public void apply(boolean state) {
        if (WifiHandler.hasWifiPermission(this.context)) {
            WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(state);
        } else {
            Log.e(TAG, "No wifi permission granted"); // TODO reminder notification
        }
    }
}
