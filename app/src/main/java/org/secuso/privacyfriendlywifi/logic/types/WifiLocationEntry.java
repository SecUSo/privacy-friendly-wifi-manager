package org.secuso.privacyfriendlywifi.logic.types;

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlywifi.logic.preconditions.CellLocationCondition;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class WifiLocationEntry extends PreconditionEntry implements Serializable {
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
            newCondition.addObserver(this);
            this.notifyChanged();

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
    public boolean equals(Object o) {
        if (o instanceof WifiLocationEntry) {
            WifiLocationEntry that = (WifiLocationEntry) o;
            return this.getSsid().equals(that.getSsid())
                    && this.getCellLocationConditions().containsAll(that.getCellLocationConditions())
                    && that.getCellLocationConditions().containsAll(this.getCellLocationConditions());
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull PreconditionEntry another) {
        if (WifiHandler.getCurrentSSID(StaticContext.getContext()).equals(this.getSsid())) {
            return -1;
        } else {
            return another instanceof WifiLocationEntry ? this.getSsid().compareTo(((WifiLocationEntry) another).getSsid()) : -1;
        }

    }
}
