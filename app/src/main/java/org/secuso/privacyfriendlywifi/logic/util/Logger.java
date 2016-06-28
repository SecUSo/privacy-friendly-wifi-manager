package org.secuso.privacyfriendlywifi.logic.util;

import android.util.Log;

/**
 *
 */
public class Logger {
    public static final int LOG_LEVEL_VERBOSE = 4;
    public static final int LOG_LEVEL_DEBUG = 3;
    public static final int LOG_LEVEL_INFO = 2;
    public static final int LOG_LEVEL_WARNING = 1;
    public static final int LOG_LEVEL_ERROR = 0;

    private static final int LOG_LEVEL = LOG_LEVEL_VERBOSE;

    public static void v(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_VERBOSE)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_DEBUG)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_INFO)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_WARNING)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_ERROR)
            Log.e(tag, msg);
    }
}
