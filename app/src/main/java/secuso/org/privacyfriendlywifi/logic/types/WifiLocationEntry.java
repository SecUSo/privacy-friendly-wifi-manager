package secuso.org.privacyfriendlywifi.logic.types;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Parcel;
import android.os.Parcelable;

import secuso.org.privacyfriendlywifi.logic.preconditions.CellLocationCondition;

/**
 *
 */
public class WifiLocationEntry implements Parcelable {
    private final String bssid;
    private final String ssid;
    private final CellLocationCondition cellLocationCondition;

    public WifiLocationEntry(String ssid, String bssid) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.cellLocationCondition = new CellLocationCondition();
    }

    public CellLocationCondition getCellLocationCondition() {
        return this.cellLocationCondition;
    }

    protected WifiLocationEntry(Parcel in) {
        bssid = in.readString();
        ssid = in.readString();
        cellLocationCondition = in.readParcelable(CellLocationCondition.class.getClassLoader());
    }

    public static final Creator<WifiLocationEntry> CREATOR = new Creator<WifiLocationEntry>() {
        @Override
        public WifiLocationEntry createFromParcel(Parcel in) {
            return new WifiLocationEntry(in);
        }

        @Override
        public WifiLocationEntry[] newArray(int size) {
            return new WifiLocationEntry[size];
        }
    };

    private static String getCurrentBssid(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(
                Context.WIFI_SERVICE);
        return wifiMan.getConnectionInfo().getBSSID();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bssid);
        dest.writeString(ssid);
        dest.writeParcelable(cellLocationCondition, flags);
    }
}
