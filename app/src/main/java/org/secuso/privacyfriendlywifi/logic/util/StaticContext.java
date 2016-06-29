package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

/**
 *
 */
public class StaticContext {
    private static Context context;

    public static void setContext(Context context) {
        if (/*StaticContext.context == null && */context != null) {
            Logger.d("TAG", "Static context set!");
            StaticContext.context = context;
        }
    }

    public static Context getContext() {
        if (StaticContext.context == null) {
            throw new NullPointerException("Context has not been set yet!");
        }
        return StaticContext.context;
    }
}
