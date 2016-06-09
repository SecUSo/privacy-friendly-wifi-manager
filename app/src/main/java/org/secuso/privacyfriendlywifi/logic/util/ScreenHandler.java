package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

/**
 *
 */
public class ScreenHandler {

    /**
     * Convert dp to px
     */
    public static int getPXFromDP(int dp, Context context) {
        return (int) (context.getResources().getDisplayMetrics().density * dp);
    }
}
