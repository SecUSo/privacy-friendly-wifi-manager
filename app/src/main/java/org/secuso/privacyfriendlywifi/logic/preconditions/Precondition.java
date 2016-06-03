package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Interface representing a precondition to check for.
 */
public class Precondition implements Parcelable {
    protected boolean isActive = true; // by default a user created precondition is active

    public Precondition() {
    }

    public Precondition(boolean isActive) {
        this.isActive = isActive;
    }

    protected Precondition(Parcel in) {
        this.isActive = in.readInt() != 0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }


    /**
     * Check whether the precondition applies.
     *
     * @return True, if the precondition applies.
     */
    boolean check(Context context, Object obj) {
        return this.isActive;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isActive ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Precondition> CREATOR = new Creator<Precondition>() {
        @Override
        public Precondition createFromParcel(Parcel in) {
            return new Precondition(in);
        }

        @Override
        public Precondition[] newArray(int size) {
            return new Precondition[size];
        }
    };
}
