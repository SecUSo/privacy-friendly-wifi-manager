package org.secuso.privacyfriendlywifi.service;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;

/**
 *
 */
public class Controller {
    private static final String TAG = Controller.class.getSimpleName();

    public static void registerReceivers() {
        Controller.unregisterReceivers();
        Logger.v(TAG, "Register all receivers.");
        AlarmReceiver.fireAndSchedule();
    }

    public static void unregisterReceivers() {
        Logger.v(TAG, "Unregister all receivers.");
        AlarmReceiver.cancelAlarm();
    }
}
