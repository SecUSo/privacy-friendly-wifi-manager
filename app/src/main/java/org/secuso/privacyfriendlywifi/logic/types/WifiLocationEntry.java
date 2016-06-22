package org.secuso.privacyfriendlywifi.logic.types;

import android.os.Parcel;
import android.os.Parcelable;

import org.secuso.privacyfriendlywifi.logic.preconditions.CellLocationCondition;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class WifiLocationEntry implements Parcelable, Serializable {
    private String ssid;
    private List<String> bssidList;
    private CellLocationCondition cellLocationCondition;

    public WifiLocationEntry(String ssid, String bssid) {
        this(ssid, Arrays.asList(new String[]{bssid}));
    }

    public WifiLocationEntry(String ssid, List<String> bssidList) {
        this.ssid = ssid;
        this.bssidList = bssidList;
        this.cellLocationCondition = new CellLocationCondition();
    }


    public String getSsid() {
        return this.ssid;
    }

    public List<String> getBssids() {
        return this.bssidList;
    }

    public CellLocationCondition getCellLocationCondition() {
        return this.cellLocationCondition;
    }

    protected WifiLocationEntry(Parcel in) {
        ssid = in.readString();
        in.readList(this.bssidList, String.class.getClassLoader());
        cellLocationCondition = in.readParcelable(CellLocationCondition.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ssid);
        dest.writeList(this.bssidList);
        dest.writeParcelable(this.cellLocationCondition, flags);
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

    @Override
    public boolean equals(Object o) {
        return o instanceof WifiLocationEntry && this.getSsid().equals(((WifiLocationEntry) o).getSsid());
    }
}
