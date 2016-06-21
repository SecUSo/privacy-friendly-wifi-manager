package org.secuso.privacyfriendlywifi.logic.effects;

/**
 * Interface for an action to happen.
 */
public interface IEffect {

    /**
     * Apply the effect using the arguments passed.
     *
     * @param state State for effect.
     */
    void apply(boolean state);
}
