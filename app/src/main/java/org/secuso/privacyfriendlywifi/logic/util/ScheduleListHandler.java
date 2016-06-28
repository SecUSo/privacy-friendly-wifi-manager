package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.util.List;
import java.util.Observable;

/**
 *
 */
public class ScheduleListHandler implements IListHandler<ScheduleEntry> {
    private static ListHandler<ScheduleEntry> list;

    public ScheduleListHandler(Context context) {
        if (ScheduleListHandler.list == null) {
            ScheduleListHandler.list = new ListHandler<>(context, ManagerService.FN_SCHEDULE_ENTRIES);
        }
    }

    public Observable getListObservable() {
        return ScheduleListHandler.list.getListObservable();
    }

    public boolean save() {
        return ScheduleListHandler.list.save();
    }

    public List<ScheduleEntry> getAll() {
        return ScheduleListHandler.list.getAll();
    }

    public ScheduleEntry get(int location) {
        return ScheduleListHandler.list.get(location);
    }

    public boolean add(ScheduleEntry newEntry) {
        return ScheduleListHandler.list.add(newEntry);
    }

    public boolean addAll(List<ScheduleEntry> newEntries) {
        return ScheduleListHandler.list.addAll(newEntries);
    }

    public boolean remove(ScheduleEntry entry) {
        return ScheduleListHandler.list.remove(entry);
    }

    public int size() {
        return ScheduleListHandler.list.size();
    }

    public int indexOf(Object o) {
        return ScheduleListHandler.list.indexOf(o);
    }

    public boolean isEmpty() {
        return ScheduleListHandler.list.isEmpty();
    }
}
