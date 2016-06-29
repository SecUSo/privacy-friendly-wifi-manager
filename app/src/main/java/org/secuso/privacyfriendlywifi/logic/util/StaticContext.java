package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;
import android.util.Log;

/**
 *
 */
public class StaticContext {
    private static Context context;

    public static void setContext(Context context) {
        if (/*StaticContext.context == null && */context != null) {
            Log.d("TAG", "Static context set!");
            StaticContext.context = context;
        }
    }

    public static Context getContext() {
        if (StaticContext.context == null) {
            throw new NullPointerException("Context not yet set!");
        }
        return StaticContext.context;
    }
}
