package org.secuso.privacyfriendlywifi.logic.types;

import android.os.Parcel;
import android.os.Parcelable;

import org.secuso.privacyfriendlywifi.logic.preconditions.CellLocationCondition;

/**
 *
 */
public class WifiLocationEntry implements Parcelable {
    private final String ssid;
    private final String bssid;
    private final CellLocationCondition cellLocationCondition;

    public WifiLocationEntry(String ssid, String bssid) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.cellLocationCondition = new CellLocationCondition();
    }

    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public CellLocationCondition getCellLocationCondition() {
        return cellLocationCondition;
    }

    /*private static String getCurrentBssid(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(
                Context.WIFI_SERVICE);
        return wifiMan.getConnectionInfo().getBSSID();
    }*/

    protected WifiLocationEntry(Parcel in) {
        ssid = in.readString();
        bssid = in.readString();
        cellLocationCondition = in.readParcelable(CellLocationCondition.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ssid);
        dest.writeString(bssid);
        dest.writeParcelable(cellLocationCondition, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
