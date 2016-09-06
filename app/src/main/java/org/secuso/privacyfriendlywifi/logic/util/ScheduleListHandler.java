package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

/**
 *
 */
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
