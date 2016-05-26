package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.os.Parcel;
import android.util.Pair;

/**
 *
 */
public class ScheduleCondition implements Precondition {
    private final int start_hour, start_minute, end_hour, end_minute;
    private boolean isActive;

    public ScheduleCondition(int start_hour, int start_minute, int end_hour, int end_minute, boolean isActive) {
        this.start_hour = start_hour;
        this.start_minute = start_minute;
        this.end_hour = end_hour;
        this.end_minute = end_minute;
        this.isActive = isActive;
    }

    public int getStartHour() {
        return start_hour;
    }

    public int getStartMinute() {
        return start_minute;
    }

    public int getEndHour() {
        return end_hour;
    }

    public int getEndMinute() {
        return end_minute;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean check(Context context, Object obj) {
        Pair<Integer, Integer> hourAndMinute = (Pair<Integer, Integer>) obj;
        // check, whether isActive and the given (current) time lies in between the start and end time
        return isActive && start_hour <= hourAndMinute.first && hourAndMinute.first <= end_hour
                && start_minute <= hourAndMinute.second && hourAndMinute.second <= end_minute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(start_hour);
        dest.writeInt(start_minute);
        dest.writeInt(end_hour);
        dest.writeInt(end_minute);
        dest.writeInt(isActive ? 1 : 0);
    }

    protected ScheduleCondition(Parcel in) {
        this.start_hour = in.readInt();
        this.start_minute = in.readInt();
        this.end_hour = in.readInt();
        this.end_minute = in.readInt();
        this.isActive = in.readInt() != 0;
    }

    public static final Creator<ScheduleCondition> CREATOR = new Creator<ScheduleCondition>() {
        @Override
        public ScheduleCondition createFromParcel(Parcel in) {
            return new ScheduleCondition(in);
        }

        @Override
        public ScheduleCondition[] newArray(int size) {
            return new ScheduleCondition[size];
        }
    };
}
