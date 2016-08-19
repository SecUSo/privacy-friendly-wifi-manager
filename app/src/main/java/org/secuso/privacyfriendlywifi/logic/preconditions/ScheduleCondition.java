package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.util.Pair;

import org.secuso.privacyfriendlywifi.logic.util.SerializationHelper;
import org.secuso.privacyfriendlywifi.logic.util.TimeHelper;

import java.io.IOException;

/**
 *
 */
public class ScheduleCondition extends Precondition {
    private int start_hour, start_minute, end_hour, end_minute;

    public ScheduleCondition(int start_hour, int start_minute, int end_hour, int end_minute) {
        initialize(start_hour, start_minute, end_hour, end_minute);
    }

    public void initialize(int start_hour, int start_minute, int end_hour, int end_minute) {
        this.start_hour = start_hour;
        this.start_minute = start_minute;
        this.end_hour = end_hour;
        this.end_minute = end_minute;
    }

    /**
     * @see SerializationHelper
     */
    public void initialize(Context context, Object[] args) throws IOException {
        if (args[0] instanceof Integer && args[1] instanceof Integer && args[2] instanceof Integer && args[3] instanceof Integer) {
            initialize((int) args[0], (int) args[1], (int) args[2], (int) args[3]);
        } else {
            throw new IOException(SerializationHelper.SERIALIZATION_ERROR);
        }
    }

    protected Object[] getPersistentFields() {
        return new Object[]{this.start_hour, this.start_minute, this.end_hour, this.end_minute};
    }

    @Override
    public boolean check(Object... obj) {
        Pair<Integer, Integer> currentTime = (Pair<Integer, Integer>) obj[0];
        // check, whether isActive and the given (current) time lies in between the start and end time
        return super.check(obj)
                && TimeHelper.inTimespan(this.start_hour, this.start_minute, this.end_hour, this.end_minute, currentTime.first, currentTime.second);
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
