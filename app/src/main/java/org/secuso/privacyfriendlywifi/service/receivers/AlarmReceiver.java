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
import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.ScheduleListHandler;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.TimeHelper;
import org.secuso.privacyfriendlywifi.service.ManagerService;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * BroadcastReceiver for own alarms. Triggers ManagerService.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static final int DEFAULT_TIMEOUT_IN_SECONDS = 60;
    private static final int ADDITIONAL_TIMEOUT_IN_SECONDS = 300;
    private static final String TAG = AlarmReceiver.class.getSimpleName();
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
            AlarmReceiver.alarmIntent = PendingIntent.getBroadcast(StaticContext.getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
    }

    /**
     * Sets a pending alarm. This alarm is either repeating (below SDK level 23) or
     * will manually schedule a new alarm after invocation.
     */
    public static void setupAlarm(int secondsToStart) {
        AlarmReceiver.cancelAlarm(); // this also initializes AlarmManager, no need to call it twice
        Logger.v(TAG, "Seconds until next alarm: " + secondsToStart);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlarmReceiver.alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + secondsToStart * 1000, alarmIntent);
        } else {
            AlarmReceiver.alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + secondsToStart * 1000, alarmIntent);
        }
    }

    /**
     * Schedule the next alarm using existing time schedule.
     */
    public static void schedule() {
        schedule(false);
    }

    /**
     * Schedule the next alarm using existing time schedule.
     *
     * @param addDelay add {@code ADDITIONAL_TIMEOUT_IN_SECONDS} to delay.
     */
    public static void schedule(boolean addDelay) {
        Logger.d(TAG, "Scheduling next alarm" + (addDelay ? " with additional delay" : "."));
        ScheduleListHandler scheduleEntries = new ScheduleListHandler();

        Calendar cal = GregorianCalendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);
        Pair<Integer, Integer> time = new Pair<>(currentHour, currentMinute);

        int diffSeconds;

        ScheduleCondition currentSchedule = null;
        // check all schedule entries, calculate necessary timeout
        for (ScheduleEntry entry : scheduleEntries.getAll()) {
            ScheduleCondition schedCond = entry.getScheduleCondition();
            if (schedCond.check(time)) {
                currentSchedule = schedCond;
                break;
            }
        }

        if (currentSchedule != null) {
            diffSeconds = TimeHelper.getTimeDifference(currentHour, currentMinute, currentSchedule.getEndHour(), currentSchedule.getEndMinute());

            if (diffSeconds < 0) { // e.g. endHour is 00:00 and currentHour is 23:00 -> change diff from -23:00 to +01:00
                diffSeconds += 86400; // 60*60*24 = one day in seconds
            }
        } else { // if there has not been any entry, we should set the timeout to its default value
            diffSeconds = AlarmReceiver.DEFAULT_TIMEOUT_IN_SECONDS;

            if (addDelay) {
                diffSeconds += AlarmReceiver.ADDITIONAL_TIMEOUT_IN_SECONDS;
            }

            // check all schedule entries, calculate necessary timeout
            for (ScheduleEntry entry : scheduleEntries.getAll()) {
                ScheduleCondition schedCond = entry.getScheduleCondition();

                if (!schedCond.check(time)) {
                    int val = TimeHelper.getTimeDifference(currentHour, currentMinute, schedCond.getStartHour(), schedCond.getStartMinute());
                    if (val < diffSeconds) {
                        diffSeconds = val;
                    }
                }
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

    /**
     * Invokes {@link ManagerService}.
     */
    public static void fire() {
        Intent startManagerService = new Intent(StaticContext.getContext(), ManagerService.class);
        startWakefulService(StaticContext.getContext(), startManagerService);
    }

    /**
     * Schedules a new alarm and invokes {@link ManagerService}.
     */
    public static void fireAndSchedule() {
        schedule();
        fire();
    }

    /**
     /**
     * Schedules a new alarm with additional delay and invokes {@link ManagerService}.
     * @param addDelay If true, {@code AlarmReceiver.ADDITIONAL_TIMEOUT_IN_SECONDS} will be added to delay.
     */
    public static void fireAndSchedule(boolean addDelay) {
        schedule(addDelay);
        fire();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "AlarmReceiver received intent.");
        StaticContext.setContext(context);
        AlarmReceiver.fireAndSchedule();
    }
}
