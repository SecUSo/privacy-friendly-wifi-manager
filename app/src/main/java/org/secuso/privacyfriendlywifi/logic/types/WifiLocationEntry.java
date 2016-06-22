package org.secuso.privacyfriendlywifi.logic.types;

import android.os.Parcel;
import android.os.Parcelable;

import org.secuso.privacyfriendlywifi.logic.preconditions.CellLocationCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class WifiLocationEntry implements Parcelable, Serializable {
    private String ssid;
    private List<CellLocationCondition> cellLocationConditions;

    public WifiLocationEntry(String ssid, String bssid) {
        this(ssid, Arrays.asList(new String[]{bssid}));
    }

    public WifiLocationEntry(String ssid, List<String> bssidList) {
        this.ssid = ssid;
        this.cellLocationConditions = new ArrayList<>();
        for (String bssid : bssidList) {
            this.addCellLocationCondition(new CellLocationCondition(bssid));
        }
    }

    public boolean addCellLocationCondition(CellLocationCondition newCondition) {
        if (!this.cellLocationConditions.contains(newCondition)) {
            this.cellLocationConditions.add(newCondition);
            return true;
        }
        return false;
    }

    public String getSsid() {
        return this.ssid;
    }

    public List<CellLocationCondition> getCellLocationConditions() {
        return this.cellLocationConditions;
    }

    protected WifiLocationEntry(Parcel in) {
        this.ssid = in.readString();
        in.readList(this.cellLocationConditions, String.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ssid);
        dest.writeList(this.cellLocationConditions);
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
        if (o instanceof WifiLocationEntry) {
            WifiLocationEntry that = (WifiLocationEntry) o;
            return this.getSsid().equals(that.getSsid())
                    && this.getCellLocationConditions().containsAll(that.getCellLocationConditions())
                    && that.getCellLocationConditions().containsAll(this.getCellLocationConditions());
        }
        return false;
    }
}
