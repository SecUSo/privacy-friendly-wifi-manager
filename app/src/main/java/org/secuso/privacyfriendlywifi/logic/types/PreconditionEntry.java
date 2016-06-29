package org.secuso.privacyfriendlywifi.logic.types;

import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public abstract class PreconditionEntry extends Observable implements Observer {
    protected void notifyChanged() {
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public void update(Observable observable, Object data) {
        this.notifyChanged();
    }
}
