package secuso.org.privacyfriendlywifi.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 *
 */
public class Controller {
    private static ScreenOnReceiver screenOnReceiver;

    public static void registerReceivers(Context context) {
        if (Controller.screenOnReceiver == null) {
            Controller.screenOnReceiver = new ScreenOnReceiver();
        }

        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(screenOnReceiver, screenStateFilter);

        AlarmReceiver.setupAlarm(context);
    }


    public static void unregisterReceivers(Context context) {
        if (Controller.screenOnReceiver != null) {
            context.unregisterReceiver(Controller.screenOnReceiver);
            Controller.screenOnReceiver = null;
        }

        AlarmReceiver.cancelAlarm(context);
    }
}
