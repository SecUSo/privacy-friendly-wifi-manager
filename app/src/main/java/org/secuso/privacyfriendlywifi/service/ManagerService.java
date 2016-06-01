package org.secuso.privacyfriendlywifi.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Pair;

import org.secuso.privacyfriendlywifi.logic.WifiToggleEffect;
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
    private static final String FN_SCHEDULE_ENTRIES = "fn_schedule_entries";
    private static final String FN_LOCATION_ENTRIES = "fn_location_entries";

    private List<ScheduleEntry> scheduleEntries;
    private List<WifiLocationEntry> wifiLocationEntries;

    public ManagerService() {
        super(ManagerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // check if Wifi is scheduled to be off
            boolean scheduleIsActive = this.checkSchedule();

            if (!scheduleIsActive) {
                // Check whether Wifi is On/Off
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                //TODO do something with:
                wifiManager.isWifiEnabled();


                // TODO Handle CellInformation to trigger WifiToggleEvent
                try {
                    Object o = FileHandler.loadObject(this, FN_LOCATION_ENTRIES, false);
                    if (o instanceof List) {
                        this.wifiLocationEntries = (List<WifiLocationEntry>) o;
                    }
                } catch (IOException e) {
                    // File does not exist
                    this.wifiLocationEntries = new ArrayList<>();
                }

                PrimitiveCellInfoTreeSet allCells = PrimitiveCellInfo.getAllCells(this);

                for (WifiLocationEntry entry : this.wifiLocationEntries) {
                    // TODO do something with the checked info
                    entry.getCellLocationCondition().check(this, allCells);
                }

                try {
                    FileHandler.storeObject(this, FN_LOCATION_ENTRIES, this.wifiLocationEntries);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            // tell everyone that we are done
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private boolean checkSchedule() {
        // load or create schedule
        try {
            Object o = FileHandler.loadObject(this, FN_SCHEDULE_ENTRIES, false);
            if (o instanceof List) {
                this.scheduleEntries = (List<ScheduleEntry>) o;
            }
        } catch (IOException e) {
            // File does not exist
            this.scheduleEntries = new ArrayList<>();
        }

        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR);
        int currentMinute = cal.get(Calendar.MINUTE);

        for (ScheduleEntry entry : this.scheduleEntries) {
            if (entry.getScheduleCondition().check(this, new Pair<Integer, Integer>(currentHour, currentMinute))) {
                WifiToggleEffect wifiToggleEffect = new WifiToggleEffect(this);
                wifiToggleEffect.apply(false); // turn off wifi
                return true; // schedule active, skip the rest
            }
        }
        return false; // no schedule active
    }
}
