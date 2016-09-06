package org.secuso.privacyfriendlywifi.logic.types;

import org.secuso.privacyfriendlywifi.logic.util.SerializationHelper;

import java.util.Observable;
import java.util.Observer;

/**
 * A serializable precondition entry.
 */
public abstract class PreconditionEntry extends SerializationHelper implements Observer, Comparable<PreconditionEntry> {
    private static final long serialVersionUID = -8344028945131554037L;

    protected void notifyChanged() {
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public void update(Observable observable, Object data) {
        this.notifyChanged();
    }
}
