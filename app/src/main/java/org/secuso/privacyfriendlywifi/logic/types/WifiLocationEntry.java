/*
 This file is part of Privacy Friendly Wifi Manager.
 Privacy Friendly Wifi Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Wifi Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Wifi Manager. If not, see <http://www.gnu.org/licenses/>.
 */

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

public class WifiLocationEntry extends PreconditionEntry implements Serializable {
    private static final long serialVersionUID = -5254647899323647079L;
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
