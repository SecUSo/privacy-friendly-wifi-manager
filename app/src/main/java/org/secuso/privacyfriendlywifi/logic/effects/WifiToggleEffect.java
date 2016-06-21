package org.secuso.privacyfriendlywifi.logic.effects;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * (De-) Activate WiFi.
 */
public class WifiToggleEffect implements IEffect {
    static final String TAG = "WifiToggleEffect";
    Context context;

    public WifiToggleEffect(Context context) {
        this.context = context;
    }

    public static boolean hasWifiPermission(Context context) {
        return (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CHANGE_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWifiPermission() {
        return WifiToggleEffect.hasWifiPermission(this.context);
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

    @Override
    public void apply(boolean state) {
        if (this.hasWifiPermission()) {
            WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(state);
        } else {
            Log.e(TAG, "No wifi permission granted"); // TODO reminder notification
        }
    }
}
