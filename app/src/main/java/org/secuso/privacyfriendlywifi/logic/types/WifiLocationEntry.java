package org.secuso.privacyfriendlywifi.logic.types;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlywifi.logic.preconditions.CellLocationCondition;
import org.secuso.privacyfriendlywifi.logic.util.SerializationHelper;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;

import java.io.IOException;
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
        initialize(ssid, bssid);
    }

    public WifiLocationEntry(String ssid, List<String> bssidList) {
        initialize(ssid, bssidList);
    }

    public void initialize(String ssid, String bssid) {
        initialize(ssid, Arrays.asList(new String[]{bssid}));
    }

    /**
     * @see SerializationHelper
     */
    public void initialize(Context context, Object[] args) throws IOException {
        if (args[0] instanceof String && args[1] instanceof List) {
            this.ssid = (String) args[0];
            this.cellLocationConditions = (List<CellLocationCondition>) args[1];
        } else {
            throw new IOException(SerializationHelper.SERIALIZATION_ERROR);
        }
    }

    public void initialize(String ssid, List<String> bssidList) {
        this.ssid = ssid;
        this.cellLocationConditions = new ArrayList<>(); // reset list

        for (String bssid : bssidList) {
            this.addCellLocationCondition(new CellLocationCondition(bssid));
        }
    }

    protected Object[] getPersistentFields() {
        return new Object[]{this.ssid, this.cellLocationConditions};
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
    public int compareTo(@NonNull PreconditionEntry that) {
        Context context = StaticContext.getContext();

        if (context != null && WifiHandler.getCurrentSSID(context).equals(this.getSsid())) {
            return -1;
        } else {
            if (that instanceof WifiLocationEntry) {
                WifiLocationEntry other = (WifiLocationEntry) that;
                return WifiHandler.getCurrentSSID(context).equals(other.getSsid()) ? 1 : this.getSsid().compareTo(other.getSsid());
            }

            return -1;
        }
    }

    @Override
    public String toString() {
        return this.getSsid();
    }
}
