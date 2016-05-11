package secuso.org.privacyfriendlywifi.logic.preconditions;

/**
 * Interface representing a precondition to check for.
 */
public interface Precondition {

    /**
     * Check whether the precondition applies.
     * @return True, if the precondition applies.
     */
    boolean check();
}
