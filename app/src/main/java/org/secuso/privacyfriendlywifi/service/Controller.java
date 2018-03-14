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

package org.secuso.privacyfriendlywifi.service;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;

/**
 * Controller to (un-) register the service.
 */
public class Controller {
    private static final String TAG = Controller.class.getSimpleName();

    public static void registerReceivers(Context context) {
        Controller.unregisterReceivers(context);
        Logger.v(TAG, "Register all receivers.");
        AlarmReceiver.fireAndSchedule(context);
    }

    public static void unregisterReceivers(Context context) {
        Logger.v(TAG, "Unregister all receivers.");
        AlarmReceiver.cancelAlarm(context);
    }
}
