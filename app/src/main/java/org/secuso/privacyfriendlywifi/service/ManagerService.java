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
import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;
import org.secuso.privacyfriendlywifi.logic.util.WifiListHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
public class ManagerService extends IntentService {
    public static final String TAG = "ManagerService";
    public static final String FN_SCHEDULE_ENTRIES = "fn_schedule_entries";
    public static final String FN_LOCATION_ENTRIES = "fn_location_entries";

    public final static String PREF_SETTINGS = "SHARED_PREF_SETTINGS";
    public final static String PREF_ENTRY_SERVICE_ACTIVE = "SHARED_PREF_ENTRY_SERVICE_ACTIVE";

    private WifiListHandler wifiListHandler;

    public ManagerService() {
        super(ManagerService.class.getSimpleName());
        this.wifiListHandler = new WifiListHandler();
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Logger.d(TAG, "Incoming intent");
        StaticContext.setContext(this);
        boolean searchedForWifi = false;

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
                                WifiHandler.scanAndUpdateWifis(context, unknownNetworks);

                                // unregister this receiver
                                try {
                                    context.unregisterReceiver(this);
                                } catch (IllegalArgumentException e) {
                                    // Log.d("TAG", "not registered");
                                }

                                // tell everyone that we are done
                                WakefulBroadcastReceiver.completeWakefulIntent(intent);
                            }
                        };

                        // unregister this receiver
                        try {
                            this.unregisterReceiver(receiver);
                        } catch (IllegalArgumentException e) {
                            // Log.d("TAG", "not registered");
                        }

                        IntentFilter filter = new IntentFilter();
                        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                        searchedForWifi = true;
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
            WifiToggleEffect wifiToggleEffect = new WifiToggleEffect();
            wifiToggleEffect.apply(determinedWifiState);
        } finally {
            if (!searchedForWifi) {
                // tell everyone that we are done
                WakefulBroadcastReceiver.completeWakefulIntent(intent);
            }
        }
    }

    private void updateCells() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo currentConnection = wifiManager.getConnectionInfo();
        String currentSsid = WifiHandler.getCleanSSID(currentConnection.getSSID());
        String currentBssid = currentConnection.getBSSID();

        for (WifiLocationEntry entry : this.wifiListHandler.getAll()) {
            if (entry.getSsid().equals(currentSsid)) {
                for (CellLocationCondition condition : entry.getCellLocationConditions()) {
                    if (condition.getBssid().equals(currentBssid)) {
                        condition.addKBestSurroundingCells(this, 3);
                        return;
                    }
                }
            }
        }
    }

    private boolean checkCells() {
        PrimitiveCellInfoTreeSet allCells = PrimitiveCellInfo.getAllCells(this);

        for (WifiLocationEntry entry : this.wifiListHandler.getAll()) {
            for (CellLocationCondition condition : entry.getCellLocationConditions()) {
                if (condition.check(allCells)) {
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
            if (entry.getScheduleCondition().check(time)) {
                return true; // schedule active, skip the rest
            }
        }

        return false; // no schedule active
    }

    public static void setActiveFlag(boolean state) {
        SharedPreferences settings = StaticContext.getContext().getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE);
        settings.edit().putBoolean(ManagerService.PREF_ENTRY_SERVICE_ACTIVE, state).apply();
    }

    public static boolean isServiceActive() {
        SharedPreferences settings = StaticContext.getContext().getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE);

        return settings.getBoolean(PREF_ENTRY_SERVICE_ACTIVE, false);
    }
}
