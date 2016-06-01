package org.secuso.privacyfriendlywifi.logic;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.secuso.privacyfriendlywifi.logic.effects.Effect;

/**
 * (De-) Activate WiFi.
 */
public class WifiToggleEffect implements Effect {
    Context context;

    public WifiToggleEffect(Context context) {
        this.context = context;
    }

    @Override
    public void apply(boolean state) {
        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);
        boolean wifiEnabled = wifiManager.isWifiEnabled();
    }
}
