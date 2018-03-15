/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.service.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Pair;

import org.secuso.privacyfriendlywifimanager.logic.preconditions.ScheduleCondition;
import org.secuso.privacyfriendlywifimanager.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.Logger;
import org.secuso.privacyfriendlywifimanager.logic.util.ScheduleListHandler;
import org.secuso.privacyfriendlywifimanager.logic.util.StaticContext;
import org.secuso.privacyfriendlywifimanager.logic.util.TimeHelper;
import org.secuso.privacyfriendlywifimanager.service.ManagerService;

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
    private static void initAlarmManager(Context context) {
        if (AlarmReceiver.alarmManager == null) {
            AlarmReceiver.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        if (AlarmReceiver.alarmIntent == null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            AlarmReceiver.alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    /**
     * Sets a pending alarm. This alarm is either repeating (below SDK level 23) or
     * will manually schedule a new alarm after invocation.
     */
    public static void setupAlarm(Context context, int secondsToStart) {
        AlarmReceiver.cancelAlarm(context); // this also initializes AlarmManager, no need to call it twice
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
    public static void schedule(Context context) {
        schedule(context, false);
    }

    /**
     * Schedule the next alarm using existing time schedule.
     *
     * @param addDelay add {@code ADDITIONAL_TIMEOUT_IN_SECONDS} to delay.
     */
    public static void schedule(Context context, boolean addDelay) {
        Logger.d(TAG, "Scheduling next alarm" + (addDelay ? " with additional delay" : "."));
        ScheduleListHandler scheduleEntries = new ScheduleListHandler(context);

        Calendar cal = GregorianCalendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);
        int currentSecond = cal.get(Calendar.SECOND);
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
            diffSeconds = TimeHelper.getTimeDifference(currentHour, currentMinute, currentSecond, currentSchedule.getEndHour(), currentSchedule.getEndMinute(), 0);
        } else { // if there has not been any entry, we should set the timeout to its default value
            diffSeconds = AlarmReceiver.DEFAULT_TIMEOUT_IN_SECONDS;

            if (addDelay) {
                diffSeconds += AlarmReceiver.ADDITIONAL_TIMEOUT_IN_SECONDS;
            }

            // check all schedule entries, calculate necessary timeout
            for (ScheduleEntry entry : scheduleEntries.getAll()) {
                ScheduleCondition schedCond = entry.getScheduleCondition();

                if (!schedCond.check(time)) {
                    int timeDifference = TimeHelper.getTimeDifference(currentHour, currentMinute, currentSecond, schedCond.getStartHour(), schedCond.getStartMinute(), 0);

                    if (timeDifference < diffSeconds) {
                        diffSeconds = timeDifference;
                    }
                }
            }
        }

        // setup alarm
        setupAlarm(context, diffSeconds);
    }

    /**
     * Cancels the pending alarm.
     */
    public static void cancelAlarm(Context context) {
        AlarmReceiver.initAlarmManager(context);
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
     * Schedules a new alarm and invokes {@link ManagerService}. Returns asynchronous.
     */
    public static void fireAndSchedule(final Context context) {
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                schedule(context);
                fire();
            }
        });

    }

    /**
     * /**
     * Schedules a new alarm with additional delay and invokes {@link ManagerService}. Returns asynchronous.
     *
     * @param addDelay If true, {@code AlarmReceiver.ADDITIONAL_TIMEOUT_IN_SECONDS} will be added to delay.
     */
    public static void fireAndSchedule(final Context context, final boolean addDelay) {
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                schedule(context, addDelay);
                fire();
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "AlarmReceiver received intent.");
        StaticContext.setContext(context);
        AlarmReceiver.fireAndSchedule(context);
    }
}
