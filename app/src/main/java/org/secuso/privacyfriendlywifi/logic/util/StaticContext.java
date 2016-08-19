package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

/**
 *
 */
public class StaticContext {
    private static Context context;
    private static final String TAG = StaticContext.class.getSimpleName();

    /**
     * Sets the static context if passed context is not null.
     *
     * @param context A context to use.
     */
    public static void setContext(Context context) {
        if (/*StaticContext.context == null && */context != null) {
            Logger.logADB("v", TAG, "Static context set!");
            StaticContext.context = context;
        }
    }

    /**
     * Returns a context.
     *
     * @return A context. This may be null.
     */
    public static Context getContext() {
        if (StaticContext.context == null) {
            Logger.logADB("e", TAG, "Context has not been set yet!");
        }

        return StaticContext.context;
    }
}
