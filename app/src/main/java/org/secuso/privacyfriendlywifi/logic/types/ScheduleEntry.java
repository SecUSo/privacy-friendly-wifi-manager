package org.secuso.privacyfriendlywifi.logic.types;

import android.os.Parcel;
import android.os.Parcelable;

import org.secuso.privacyfriendlywifi.logic.preconditions.ScheduleCondition;

import java.io.Serializable;

/**
 *
 */
public class ScheduleEntry extends PreconditionEntry implements Parcelable, Serializable {
    private String title;
    private ScheduleCondition scheduleCondition;

    public ScheduleEntry(String title, int start_hour, int start_minute, int end_hour, int end_minute) {
        this.title = title;
        scheduleCondition = new ScheduleCondition(start_hour, start_minute, end_hour, end_minute);
    }

    protected ScheduleEntry(Parcel in) {
        title = in.readString();
        scheduleCondition = in.readParcelable(ScheduleCondition.class.getClassLoader());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ScheduleCondition getScheduleCondition() {
        return scheduleCondition;
    }

    public void setScheduleCondition(ScheduleCondition scheduleCondition) {
        this.scheduleCondition = scheduleCondition;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeParcelable(scheduleCondition, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ScheduleEntry> CREATOR = new Creator<ScheduleEntry>() {
        @Override
        public ScheduleEntry createFromParcel(Parcel in) {
            return new ScheduleEntry(in);
        }

        @Override
        public ScheduleEntry[] newArray(int size) {
            return new ScheduleEntry[size];
        }
    };
}
