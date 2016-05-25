package secuso.org.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.os.Parcel;

/**
 *
 */
public class ScheduleCondition implements Precondition {
    @Override
    public boolean check(Context context) {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
