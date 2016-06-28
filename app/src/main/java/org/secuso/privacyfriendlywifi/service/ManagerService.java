package org.secuso.privacyfriendlywifi.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Pair;

import org.secuso.privacyfriendlywifi.logic.effects.WifiToggleEffect;
import org.secuso.privacyfriendlywifi.logic.preconditions.CellLocationCondition;
import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfo;
import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfoTreeSet;
import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.FileHandler;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;

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

    public final static String PREF_SETTINGS = "SHARED_PREF_SETTINGS";
    public final static String PREF_ENTRY_SERVICE_ACTIVE = "SHARED_PREF_ENTRY_SERVICE_ACTIVE";

    public ManagerService() {
        super(ManagerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            boolean determinedWifiState = false; // check if Wifi is scheduled to be on (true) / off (false)

            if (this.checkSchedule()) {
                // Case 1: Wifi scheduled to be off, don't care about anything else
                determinedWifiState = false; // TODO establish a more intuitive visualisation in UI
            } else if (WifiHandler.hasWifiPermission(this)) {
                if (WifiHandler.isWifiEnabled(this)) {
                    // Case 2: Wifi ON,disconnected (should be off? -> no known cells in range)

                    if (!WifiHandler.isWifiConnected(this)) {
                        final List<WifiLocationEntry> unknownNetworks = new ArrayList<>();

                        final BroadcastReceiver receiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent i) {
                                // fetch search results
                                WifiHandler.scanAndUpdateWifis(context, WifiListHandler.getWifiLocationEntries(this), unknownNetworks); // TODO Commit & push WifiListHandler
                            }
                        };

                        IntentFilter filter = new IntentFilter();
                        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                        this.registerReceiver(receiver, filter);
                        determinedWifiState = this.checkCells();
                    } else {
                        // Case 3: Wifi ON,connected (ok to be on -> update cells)
                        this.updateCells();
                        determinedWifiState = true;
                    }
                } else {
                    // Case 4: Wifi OFF (should be on? -> known cells in range)
                    determinedWifiState = this.checkCells();
                }
            }

            // apply state to wifi
            WifiToggleEffect wifiToggleEffect = new WifiToggleEffect(this);
            wifiToggleEffect.apply(determinedWifiState);
        } finally {
            // tell everyone that we are done
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    public static List<WifiLocationEntry> getWifiLocationEntries(Context context) {
        try {
            Object o = FileHandler.loadObject(context, FN_LOCATION_ENTRIES, false);
            return (List<WifiLocationEntry>) o;
        } catch (IOException e) {
            // File does not exist
            return new ArrayList<>();
        }
    }

    public static boolean saveWifiLocationEntries(Context context, List<WifiLocationEntry> wifiLocationEntries) {
        try {
            FileHandler.storeObject(context, FN_LOCATION_ENTRIES, wifiLocationEntries);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void updateCells() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo currentConnection = wifiManager.getConnectionInfo();
        String currentSsid = WifiHandler.getCleanSSID(currentConnection.getSSID());
        String currentBssid = currentConnection.getBSSID();
        List<WifiLocationEntry> wifiLocationEntries = ManagerService.getWifiLocationEntries(this);

        for (WifiLocationEntry entry : wifiLocationEntries) {
            if (entry.getSsid().equals(currentSsid)) {
                for (CellLocationCondition condition : entry.getCellLocationConditions()) {
                    if (condition.getBssid().equals(currentBssid)) {
                        condition.addKBestSurroundingCells(this, 3);

                        ManagerService.saveWifiLocationEntries(this, wifiLocationEntries);

                        return;
                    }
                }
            }
        }
    }

    private boolean checkCells() {
        PrimitiveCellInfoTreeSet allCells = PrimitiveCellInfo.getAllCells(this);

        for (WifiLocationEntry entry : ManagerService.getWifiLocationEntries(this)) {
            for (CellLocationCondition condition : entry.getCellLocationConditions()) {
                if (condition.check(this, allCells)) {
                    return true;
                }
            }
        }

        return false;
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

    public static void setRunningFlag(Context context, boolean state) {
        SharedPreferences settings = context.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE);
        settings.edit().putBoolean(ManagerService.PREF_ENTRY_SERVICE_ACTIVE, state).apply();
    }

    public static boolean isServiceRunning(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE);

        return settings.getBoolean(PREF_ENTRY_SERVICE_ACTIVE, false);
    }
}
