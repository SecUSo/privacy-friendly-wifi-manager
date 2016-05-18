package secuso.org.privacyfriendlywifi.logic.effects;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * (De-) Activate WiFi.
 */
public class WiFiToggleEffect implements Effect {
    Context context;

    public WiFiToggleEffect(Context context) {
        this.context = context;
    }

    @Override
    public void apply(boolean state) {
        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);
        boolean wifiEnabled = wifiManager.isWifiEnabled();
    }
}
