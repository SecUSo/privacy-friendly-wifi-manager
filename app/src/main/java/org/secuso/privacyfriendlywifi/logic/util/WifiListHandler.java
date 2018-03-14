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

package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

public class WifiListHandler extends SerializationHelper implements IListHandler<WifiLocationEntry> {
    private static final long serialVersionUID = -7094227865638670543L;
    private static ListHandler<WifiLocationEntry> list;

    /**
     * Creates a new WifiListHandler. All instances work on the same underlying list.
     * @param context A context to use.
     */
    public WifiListHandler(Context context) {
        if (list == null) {
            WifiListHandler.list = new ListHandler<>(context, ManagerService.FN_LOCATION_ENTRIES);
        }
    }

    /**
     * @see SerializationHelper
     */
    public void initialize(Context context, Object[] args) throws IOException {
        if (args[0] instanceof ListHandler) {
            WifiListHandler.list = (ListHandler<WifiLocationEntry>) args[0];
        } else {
            throw new IOException(SerializationHelper.SERIALIZATION_ERROR);
        }
    }

    @Override
    protected Object[] getPersistentFields() {
        return new Object[]{list};
    }

    /**
     * @see IListHandler
     */
    public void addObserver(Observer observer) {
        WifiListHandler.list.addObserver(observer);
    }

    /**
     * @see IListHandler
     */
    public boolean save() {
        return WifiListHandler.list.save();
    }

    /**
     * @see IListHandler
     */
    public List<WifiLocationEntry> getAll() {
        return WifiListHandler.list.getAll();
    }

    /**
     * @see IListHandler
     */
    public WifiLocationEntry get(int location) {
        return WifiListHandler.list.get(location);
    }

    /**
     * @see IListHandler
     */
    public boolean add(WifiLocationEntry newEntry) {
        return WifiListHandler.list.add(newEntry);
    }

    /**
     * @see IListHandler
     */
    public boolean addAll(List<WifiLocationEntry> newEntries) {
        return WifiListHandler.list.addAll(newEntries);
    }

    /**
     * @see IListHandler
     */
    public void sort() {
        WifiListHandler.list.sort();
    }

    /**
     * @see IListHandler
     */
    public boolean remove(WifiLocationEntry entry) {
        return WifiListHandler.list.remove(entry);
    }

    /**
     * @see IListHandler
     */
    public int size() {
        return WifiListHandler.list.size();
    }

    /**
     * @see IListHandler
     */
    public int indexOf(Object o) {
        return WifiListHandler.list.indexOf(o);
    }

    /**
     * @see IListHandler
     */
    public boolean isEmpty() {
        return WifiListHandler.list.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<WifiLocationEntry> entries = this.getAll();

        sb.append("### Wi-Fi List ###\n");
        for (int i = 0; i < entries.size(); i++) {
            sb.append("[").append(entries.get(i).toString()).append("]\n");
        }

        return sb.toString();
    }
}
