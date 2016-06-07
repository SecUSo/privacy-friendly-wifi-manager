package org.secuso.privacyfriendlywifi.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Pair;

import org.secuso.privacyfriendlywifi.logic.effects.WifiToggleEffect;
import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfo;
import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfoTreeSet;
import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
public class ManagerService extends IntentService {
    public static final String FN_SCHEDULE_ENTRIES = "fn_schedule_entries";
    public static final String FN_LOCATION_ENTRIES = "fn_location_entries";

    public ManagerService() {
        super(ManagerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            boolean wifiState = false; // check if Wifi is scheduled to be on (true) / off (false)
            if (this.checkSchedule()) {
                wifiState = true;
            } else {
                if (!WifiToggleEffect.isWifiConnected(this)) {
                    // Check whether Wifi is On/Off
                    wifiState = WifiToggleEffect.hasWifiPermission(getApplicationContext()) && checkCells();
                } else {
                    // Update  best cells
                    updateCells();
                }
            }

            // apply state to wifi
            WifiToggleEffect wifiToggleEffect = new WifiToggleEffect(this);
            wifiToggleEffect.apply(wifiState);
        } finally {
            // tell everyone that we are done
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void updateCells() {
        List<WifiLocationEntry> wifiLocationEntries;
        boolean modified = false;
        try {
            Object o = FileHandler.loadObject(this, FN_LOCATION_ENTRIES, false);
            wifiLocationEntries = (List<WifiLocationEntry>) o;
        } catch (IOException e) {
            // File does not exist
            wifiLocationEntries = new ArrayList<>();
        }

        for (WifiLocationEntry entry : wifiLocationEntries) {
            modified |= entry.getCellLocationCondition().addKBestSurroundingCells(this, 3);
        }

        if (modified) {
            try {
                FileHandler.storeObject(this, FN_LOCATION_ENTRIES, wifiLocationEntries);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkCells() {
        boolean active = false;
        List<WifiLocationEntry> wifiLocationEntries;
        PrimitiveCellInfoTreeSet allCells = PrimitiveCellInfo.getAllCells(this);

        try {
            Object o = FileHandler.loadObject(this, FN_LOCATION_ENTRIES, false);
            wifiLocationEntries = (List<WifiLocationEntry>) o;
        } catch (IOException e) {
            // File does not exist
            wifiLocationEntries = new ArrayList<>();
        }

        for (WifiLocationEntry entry : wifiLocationEntries) {
            if (entry.getCellLocationCondition().check(this, allCells)) {
                active = true;
                break;
            }
        }

        return active;
    }

    private boolean checkSchedule() {
        List<ScheduleEntry> scheduleEntries;

        // load or create schedule
        try {
            Object o = FileHandler.loadObject(this, FN_SCHEDULE_ENTRIES, false);
            scheduleEntries = (List<ScheduleEntry>) o;
        } catch (IOException e) {
            // File does not exist
            scheduleEntries = new ArrayList<>();
        }

        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR);
        int currentMinute = cal.get(Calendar.MINUTE);
        Pair<Integer, Integer> time = new Pair<>(currentHour, currentMinute);

        for (ScheduleEntry entry : scheduleEntries) {
            if (entry.getScheduleCondition().check(this, time)) {
                return true; // schedule active, skip the rest
            }
        }

        return false; // no schedule active
    }
}