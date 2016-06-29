package org.secuso.privacyfriendlywifi.service;

import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;
import org.secuso.privacyfriendlywifi.service.receivers.ScreenOnReceiver;

/**
 *
 */
public class Controller {
    private static final String TAG = "Controller";
    private static ScreenOnReceiver screenOnReceiver;

    public static void registerReceivers() {
        Controller.unregisterReceivers();
        Log.i(TAG, "Register all receivers.");

        if (Controller.screenOnReceiver == null) {
            Controller.screenOnReceiver = new ScreenOnReceiver();
        }

        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        StaticContext.getContext().registerReceiver(Controller.screenOnReceiver, screenStateFilter);

        AlarmReceiver.schedule();
    }


    public static void unregisterReceivers() {
        Log.i(TAG, "Unregister all receivers.");
        if (Controller.screenOnReceiver != null) {
            try {
                StaticContext.getContext().unregisterReceiver(Controller.screenOnReceiver);
            } catch (IllegalArgumentException e) {
                // expected in case receiver is not registered
            } finally {
                Controller.screenOnReceiver = null;
            }
        }

        AlarmReceiver.cancelAlarm();
    }
}
