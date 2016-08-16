package org.secuso.privacyfriendlywifi.logic.types;

import android.content.Context;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlywifi.logic.preconditions.ScheduleCondition;
import org.secuso.privacyfriendlywifi.logic.util.SerializationHelper;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 */
public class ScheduleEntry extends PreconditionEntry implements Serializable {
    private String title;
    private int start_hour;
    private int start_minute;
    private int end_hour;
    private int end_minute;
    private ScheduleCondition scheduleCondition;

    public ScheduleEntry(String title, int start_hour, int start_minute, int end_hour, int end_minute) {
        initialize(title, start_hour, start_minute, end_hour, end_minute);
    }


    public void initialize(Context context, Object[] args) throws IOException {
        if (args[0] instanceof String && args[1] instanceof Integer && args[2] instanceof Integer && args[3] instanceof Integer && args[4] instanceof Integer) {
            initialize((String) args[0], (int) args[1], (int) args[2], (int) args[3], (int) args[4]);
        } else {
            throw new IOException(SerializationHelper.SERIALIZATION_ERROR);
        }
    }

    public void initialize(String title, int start_hour, int start_minute, int end_hour, int end_minute) {
        this.title = title;
        this.start_hour = start_hour;
        this.start_minute = start_minute;
        this.end_hour = end_hour;
        this.end_minute = end_minute;
        this.scheduleCondition = new ScheduleCondition(start_hour, start_minute, end_hour, end_minute);
        this.scheduleCondition.addObserver(this);
    }

    protected Object[] getPersistentFields() {
        return new Object[]{this.title, this.start_hour, this.start_minute, this.end_hour, this.end_minute};
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.notifyChanged();
    }

    public ScheduleCondition getScheduleCondition() {
        return scheduleCondition;
    }

    public void setScheduleCondition(ScheduleCondition scheduleCondition) {
        this.scheduleCondition = scheduleCondition;
        this.notifyChanged();
    }

    @Override
    public int compareTo(@NonNull PreconditionEntry another) {
        ScheduleCondition otherSchedCond;
        ScheduleCondition ownSchedCond = this.getScheduleCondition();

        if (another instanceof ScheduleEntry) {
            otherSchedCond = ((ScheduleEntry) another).getScheduleCondition();
        } else {
            return -1;
        }

        return ownSchedCond.getStartHour() < otherSchedCond.getStartHour() ? -1 : 1;
    }

    @Override
    public String toString() {
        ScheduleCondition schedCond = this.getScheduleCondition();
        return "Start: " + schedCond.getStartHour() + ":" + schedCond.getStartMinute() +
                "; End: " + schedCond.getEndHour() + ":" + schedCond.getEndMinute();
    }
}