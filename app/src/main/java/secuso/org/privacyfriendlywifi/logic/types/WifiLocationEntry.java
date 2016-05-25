package secuso.org.privacyfriendlywifi.logic.types;

import android.content.Context;
import android.net.wifi.WifiManager;

import secuso.org.privacyfriendlywifi.logic.preconditions.CellLocationCondition;

/**
 *
 */
public class WifiLocationEntry {
    private final Context context;
    private final String bssid;
    private final String ssid;
    private final CellLocationCondition cellLocationCondition;

    public WifiLocationEntry(Context context, String ssid, String bssid) {
        this.context = context;
        this.ssid = ssid;
        this.bssid = bssid;
        this.cellLocationCondition = new CellLocationCondition();
    }

    private static String getCurrentBssid(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(
                Context.WIFI_SERVICE);
        return wifiMan.getConnectionInfo().getBSSID();
    }
}
