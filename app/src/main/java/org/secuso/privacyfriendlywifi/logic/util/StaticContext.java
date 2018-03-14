/*
 This file is part of Privacy Friendly Wifi Manager.
 Privacy Friendly Wifi Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Wifi Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Wifi Manager. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

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
