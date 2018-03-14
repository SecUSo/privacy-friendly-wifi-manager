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

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

public class ScheduleListHandler extends SerializationHelper implements IListHandler<ScheduleEntry> {
    private static final long serialVersionUID = -6410767487856461175L;
    private static ListHandler<ScheduleEntry> list;

    /**
     * Creates a new ScheduleListHandler. All instances work on the same underlying list.
     * @param context A context to use.
     */
    public ScheduleListHandler(Context context) {
        if (ScheduleListHandler.list == null) {
            ScheduleListHandler.list = new ListHandler<>(context, ManagerService.FN_SCHEDULE_ENTRIES);
        }
    }

    /**
     * @see SerializationHelper
     */
    public void initialize(Context context, Object[] args) throws IOException {
        if (args[0] instanceof ListHandler) {
            ScheduleListHandler.list = (ListHandler<ScheduleEntry>) args[0];
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
        ScheduleListHandler.list.addObserver(observer);
    }

    /**
     * @see IListHandler
     */
    public boolean save() {
        return ScheduleListHandler.list.save();
    }

    /**
     * @see IListHandler
     */
    public List<ScheduleEntry> getAll() {
        return ScheduleListHandler.list.getAll();
    }

    /**
     * @see IListHandler
     */
    public ScheduleEntry get(int position) {
        return ScheduleListHandler.list.get(position);
    }

    /**
     * @see IListHandler
     */
    public boolean add(ScheduleEntry newEntry) {
        return ScheduleListHandler.list.add(newEntry);
    }

    /**
     * @see IListHandler
     */
    public boolean addAll(List<ScheduleEntry> newEntries) {
        return ScheduleListHandler.list.addAll(newEntries);
    }

    /**
     * @see IListHandler
     */
    public void sort() {
        ScheduleListHandler.list.sort();
    }

    /**
     * @see IListHandler
     */
    public boolean remove(ScheduleEntry entry) {
        return ScheduleListHandler.list.remove(entry);
    }

    /**
     * @see IListHandler
     */
    public int size() {
        return ScheduleListHandler.list.size();
    }

    /**
     * @see IListHandler
     */
    public int indexOf(Object o) {
        return ScheduleListHandler.list.indexOf(o);
    }

    /**
     * @see IListHandler
     */
    public boolean isEmpty() {
        return ScheduleListHandler.list.isEmpty();
    }
}
