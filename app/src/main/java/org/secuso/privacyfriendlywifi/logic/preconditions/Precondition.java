package org.secuso.privacyfriendlywifi.logic.preconditions;

import java.io.Serializable;
import java.util.Observable;

/**
 * Interface representing a precondition to check for.
 */
public class Precondition extends Observable implements Serializable {
    protected boolean isActive; // by default a user created precondition is active

    public Precondition() {
        this(true);
    }

    public Precondition(boolean isActive) {
        this.isActive = isActive;
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
    boolean check(Object... obj) {
        return this.isActive;
    }
}
