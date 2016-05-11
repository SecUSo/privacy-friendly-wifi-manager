package secuso.org.privacyfriendlywifi.logic.effects;

/**
 * Interface for an action to happen.
 */
public interface Effect {

    /**
     * Apply the effect using the arguments passed.
     * @param args Arguments for the effect.
     */
    void apply(Object args);
}
