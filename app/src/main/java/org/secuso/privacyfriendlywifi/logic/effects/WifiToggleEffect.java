package org.secuso.privacyfriendlywifi.logic.effects;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;

/**
 * (De-) Activate WiFi.
 */
public class WifiToggleEffect implements IEffect {
    static final String TAG = "WifiToggleEffect";

    @Override
    public void apply(boolean state) {
        if (WifiHandler.hasWifiPermission(StaticContext.getContext())) {
            WifiManager wifiManager = (WifiManager) StaticContext.getContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(state);
        } else {
            Logger.e(TAG, "No wifi permission granted"); // TODO reminder notification
        }
    }
}
