package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Observable;

/**
 * Interface representing a precondition to check for.
 */
public class Precondition extends Observable implements Parcelable, Serializable {
    protected boolean isActive = true; // by default a user created precondition is active

    public Precondition() {
    }

    public Precondition(boolean isActive) {
        this.isActive = isActive;
    }

    protected Precondition(Parcel in) {
        this.isActive = in.readInt() != 0;
    }

    protected void notifyChanged() {
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Checks the active state of the precondition.
     *
     * @return Returns the active state of the precondition.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active state of the precondition.
     * If a precondition is not active, {@code check(Object obj)} returns always false.
     *
     * @param isActive True if the precondition should be evaluated, false otherwise.
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        this.notifyChanged();
    }


    /**
     * Check whether the precondition applies.
     *
     * @return True, if the precondition applies.
     */
    boolean check(Object obj) {
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
