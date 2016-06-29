package org.secuso.privacyfriendlywifi.service.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Pair;

import org.secuso.privacyfriendlywifi.logic.preconditions.ScheduleCondition;
import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.util.ScheduleListHandler;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.util.Calendar;

/**
 * BroadcastReceiver for own alarms. Triggers ManagerService.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static final int TIMEOUT_IN_SECONDS = 60;
    private static AlarmManager alarmManager;
    private static PendingIntent alarmIntent;


    /**
     * Initializes alarmManager and alarmIntent instance variables.
     */
    private static void initAlarmManager() {
        if (AlarmReceiver.alarmManager == null) {
            AlarmReceiver.alarmManager = (AlarmManager) StaticContext.getContext().getSystemService(Context.ALARM_SERVICE);
        }

        if (AlarmReceiver.alarmIntent == null) {
            Intent intent = new Intent(StaticContext.getContext(), AlarmReceiver.class);
            AlarmReceiver.alarmIntent = PendingIntent.getBroadcast(StaticContext.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    /**
     * Sets a pending alarm. This alarm is either repeating (below SDK level 23) or
     * will manually schedule a new alarm after invocation.
     */
    public static void setupAlarm(int secondsToStart) {
        AlarmReceiver.initAlarmManager();

        // in case of externally triggered setup function -> remove old alarms
        AlarmReceiver.alarmManager.cancel(AlarmReceiver.alarmIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlarmReceiver.alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, secondsToStart * 1000, alarmIntent);
        } else {
            AlarmReceiver.alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + secondsToStart * 1000, alarmIntent);
        }
    }

    /**
     * Schedule the next alarm using existing time schedule.
     */
    public static void schedule() {
        ScheduleListHandler scheduleEntries = new ScheduleListHandler();

        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR);
        int currentMinute = cal.get(Calendar.MINUTE);
        Pair<Integer, Integer> time = new Pair<>(currentHour, currentMinute);

        int endHour = 0;
        int endMinute = 0;

        boolean foundActiveSchedule = false;

        // check all schedule entries, calculate necessary timeout
        for (ScheduleEntry entry : scheduleEntries.getAll()) {
            ScheduleCondition schedCond = entry.getScheduleCondition();
            if (schedCond.check(time)) {
                endHour = schedCond.getEndHour();
                endMinute = schedCond.getEndMinute();
                foundActiveSchedule = true;
                break;
            }
        }

        // if there has not been any entry, we should set the timeout to its default value
        int diffSeconds = AlarmReceiver.TIMEOUT_IN_SECONDS;

        if (foundActiveSchedule) {
            diffSeconds = ((endHour - currentHour) * 60 + (endMinute - currentMinute)) * 60;

            if (diffSeconds < 0) { // e.g. endHour is 00:00 and currentHour is 23:00 -> change diff from -23:00 to +01:00
                diffSeconds += 1440; // one day in minutes
            }
        }

        // setup alarm
        setupAlarm(diffSeconds);
    }

    /**
     * Cancels the pending alarm.
     */
    public static void cancelAlarm() {
        AlarmReceiver.initAlarmManager();
        AlarmReceiver.alarmManager.cancel(AlarmReceiver.alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        StaticContext.setContext(context);

        Intent startManagerService = new Intent(context, ManagerService.class);
        startWakefulService(context, startManagerService);

        AlarmReceiver.schedule();
    }
}
