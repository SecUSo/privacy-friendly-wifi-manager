package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

/**
 * Util functions regarding screen and density.
 */
public class ScreenHandler {

    /**
     * Converts dp to px
     * @param dp Density points.
     * @param context A context to use.
     * @return Pixels.
     */
    public static int getPXFromDP(int dp, Context context) {
        return (int) (context.getResources().getDisplayMetrics().density * dp);
    }
}
