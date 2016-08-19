package org.secuso.privacyfriendlywifi.logic.util;

/**
 * Listener for dialog close.
 */
public interface IOnDialogClosedListener {

    /**
     * Callback for dialog close.
     * @param returnCode Either {@code DialogInterface.BUTTON_POSITIVE} or {@code DialogInterface.BUTTON_NEGATIVE}.
     * @param returnValue Item picked in dialog.
     */
    void onDialogClosed(int returnCode, Object... returnValue);
}
