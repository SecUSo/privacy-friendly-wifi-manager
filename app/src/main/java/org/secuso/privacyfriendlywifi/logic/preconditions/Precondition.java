package org.secuso.privacyfriendlywifi.logic.preconditions;

import org.secuso.privacyfriendlywifi.logic.util.SerializationHelper;

import java.io.Serializable;

/**
 * Interface representing a precondition to check for.
 */
public abstract class Precondition extends SerializationHelper implements Serializable {
    private static final long serialVersionUID = 6055669715368490046L;
    protected boolean isActive; // by default a user created precondition is active

    /**
     * Creates a new Precondition. This is active by default.
     */
    public Precondition() {
        this(true);
    }

    /**
     * Creates a new Precondition.
     * @param isActive True if the precondition should be active, false otherwise.
     */
    public Precondition(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Notifies listeners about a changed data set.
     */
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
