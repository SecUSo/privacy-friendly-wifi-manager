package org.secuso.privacyfriendlywifi.logic.util;

/**
 * Listener for delete mode in a RemovableRecyclerView.
 */
public interface IOnDeleteModeChangedListener {
    /**
     * Set the delete mode.
     *
     * @param isActive True if delete mode should be active, false otherwise.
     */
    void setDeleteModeActive(boolean isActive);

    /**
     * Returns true if delete mode should be active, false otherwise.
     *
     * @return True if delete mode should be active, false otherwise.
     */
    boolean isDeleteModeActive();
}
