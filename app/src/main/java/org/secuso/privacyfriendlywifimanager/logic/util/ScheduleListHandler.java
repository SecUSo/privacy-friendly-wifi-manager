/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifimanager.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifimanager.service.ManagerService;

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
