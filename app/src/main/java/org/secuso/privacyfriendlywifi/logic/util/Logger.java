package org.secuso.privacyfriendlywifi.logic.util;

import android.util.Log;

/**
 *
 */
public class Logger {
    private static final boolean IS_ACTIVE = false;

    public static void v(String tag, String msg) {
        if (IS_ACTIVE)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (IS_ACTIVE)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (IS_ACTIVE)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (IS_ACTIVE)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (IS_ACTIVE)
            Log.e(tag, msg);
    }
}
