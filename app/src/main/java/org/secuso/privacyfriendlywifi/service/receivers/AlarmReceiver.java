package org.secuso.privacyfriendlywifi.service.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 *
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static final int TIMEOUT_IN_SECONDS = 5;
    private static AlarmManager alarmManager;
    private static PendingIntent alarmIntent;


    private static void initAlarmManager(Context context) {
        if (AlarmReceiver.alarmManager == null) {
            AlarmReceiver.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        if (AlarmReceiver.alarmIntent == null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            AlarmReceiver.alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public static void setupAlarm(Context context) {
        AlarmReceiver.initAlarmManager(context);

        // in case of externally triggered setup function -> remove old alarms
        AlarmReceiver.alarmManager.cancel(AlarmReceiver.alarmIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlarmReceiver.alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmReceiver.TIMEOUT_IN_SECONDS * 1000, alarmIntent);
        } else {
            AlarmReceiver.alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmReceiver.TIMEOUT_IN_SECONDS * 1000, alarmIntent);
        }
    }

    public static void cancelAlarm(Context context) {
        AlarmReceiver.initAlarmManager(context);
        AlarmReceiver.alarmManager.cancel(AlarmReceiver.alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startManagerService = new Intent(context, ManagerService.class);
        startWakefulService(context, startManagerService);

        // Set next alarm (required for > Android 6 (support for Doze))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlarmReceiver.initAlarmManager(context);
            AlarmReceiver.alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, TIMEOUT_IN_SECONDS * 1000, alarmIntent);
        }
    }
}
