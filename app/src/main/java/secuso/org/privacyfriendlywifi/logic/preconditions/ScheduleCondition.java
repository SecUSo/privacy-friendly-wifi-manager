package secuso.org.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.os.Parcel;

/**
 *
 */
public class ScheduleCondition implements Precondition {


    @Override
    public boolean check(Context context, Object obj) {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected ScheduleCondition(Parcel in) {

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
