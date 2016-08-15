package org.secuso.privacyfriendlywifi.service;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;

/**
 *
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
