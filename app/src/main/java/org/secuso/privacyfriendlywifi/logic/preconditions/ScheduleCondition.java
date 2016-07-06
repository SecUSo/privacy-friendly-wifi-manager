package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.util.Pair;

/**
 *
 */
public class ScheduleCondition extends Precondition {
    private final int start_hour, start_minute, end_hour, end_minute;

    public ScheduleCondition(int start_hour, int start_minute, int end_hour, int end_minute) {
        super();
        this.start_hour = start_hour;
        this.start_minute = start_minute;
        this.end_hour = end_hour;
        this.end_minute = end_minute;
    }

    @Override
    public boolean check(Object obj) {
        Pair<Integer, Integer> hourAndMinute = (Pair<Integer, Integer>) obj;
        // check, whether isActive and the given (current) time lies in between the start and end time
        return super.check(obj)
                && this.start_hour <= hourAndMinute.first && hourAndMinute.first <= this.end_hour
                && this.start_minute <= hourAndMinute.second && hourAndMinute.second <= this.end_minute;
    }

    public int getStartHour() {
        return this.start_hour;
    }

    public int getStartMinute() {
        return this.start_minute;
    }

    public int getEndHour() {
        return this.end_hour;
    }

    public int getEndMinute() {
        return this.end_minute;
    }
}
