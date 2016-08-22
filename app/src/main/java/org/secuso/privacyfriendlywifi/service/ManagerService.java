package org.secuso.privacyfriendlywifi.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
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
import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;
import org.secuso.privacyfriendlywifi.logic.util.WifiListHandler;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Service that handles effects of preconditions.
 */
public class ManagerService extends IntentService {
    public static final String TAG = ManagerService.class.getSimpleName();
    public static final String FN_SCHEDULE_ENTRIES = "fn_schedule_entries";
    public static final String FN_LOCATION_ENTRIES = "fn_location_entries";

    private WifiListHandler wifiListHandler;

    public ManagerService() {
        super(ManagerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Logger.v(TAG, "Incoming intent");
        StaticContext.setContext(this);

        this.wifiListHandler = new WifiListHandler(this);

        boolean determinedWifiState = false; // check if Wifi is scheduled to be on (true) / off (false)

        if (WifiHandler.hasWifiPermission(this)) {
            if (this.checkSchedule()) {
                // Case 1: Wifi scheduled to be off, don't care about anything else
                Logger.d(TAG, "Wifi will be shut down according to schedule.");
                determinedWifiState = false; // TODO establish a more intuitive visualisation in UI
            } else if (SettingsEntry.hasCoarseLocationPermission(this)) {
                if (WifiHandler.isWifiEnabled(this)) {

                    // Case 2: Wifi ON,disconnected (should be off? -> no known cells in range)
                    Intent startWifiUpdaterService = new Intent(StaticContext.getContext(), WifiUpdaterService.class);
                    startService(startWifiUpdaterService);

                    // Case 3: Wifi ON,connected (ok to be on -> update cells)
                    if (WifiHandler.isWifiConnected(this)) {
                        if (this.wifiListHandler.size() > 0 && !this.updateCells()) { // only update if Wi-Fis have been added
                            Logger.v(TAG, "No new cell -> delay next alarm.");
                            AlarmReceiver.schedule(this, true); // if no cell has been added -> increment delay until alarm
                        }

                        determinedWifiState = true;
                    } else {
                        determinedWifiState = this.checkCells();
                    }
                } else {
                    // Case 4: Wifi OFF (should be on? -> known cells in range)
                    determinedWifiState = this.checkCells();
                }
            }

            Logger.d(TAG, "Setting wifi state: " + determinedWifiState + "| Enabled: " + WifiHandler.isWifiEnabled(this) + "| Connected: " + WifiHandler.isWifiConnected(this));
            // apply state to wifi
            WifiToggleEffect wifiToggleEffect = new WifiToggleEffect();
            wifiToggleEffect.apply(determinedWifiState);
        }

        Logger.flush();

        // tell everyone that we are done
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    private boolean updateCells() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo currentConnection = wifiManager.getConnectionInfo();
        String currentSsid = WifiHandler.getCleanSSID(currentConnection.getSSID());
        String currentBssid = currentConnection.getBSSID();

        List<WifiLocationEntry> wifis = this.wifiListHandler.getAll();

        for (int i = 0; i < wifis.size(); i++) { // concurrent modification is okay, since new conditions are appended at the end
            WifiLocationEntry entry = wifis.get(i);
            if (entry.getSsid().equals(currentSsid)) {
                List<CellLocationCondition> cellLocationConditions = entry.getCellLocationConditions();
                for (int j = 0; j < cellLocationConditions.size(); j++) { // concurrent modification is okay, since new conditions are appended at the end
                    CellLocationCondition condition = cellLocationConditions.get(j);
                    if (condition.getBssid().equals(currentBssid)) {
                        Logger.d(TAG, "Adding new cells for: " + entry.getSsid());
                        return condition.addKBestSurroundingCells(this, 3);
                    }
                }
            }
        }

        return false;
    }

    private boolean checkCells() {
        PrimitiveCellInfoTreeSet allCells = PrimitiveCellInfo.getAllCells(this);
        boolean respectSignalStrength = SettingsEntry.shouldRespectSignalStrength(this);

        List<WifiLocationEntry> wifis = this.wifiListHandler.getAll();

        for (int i = 0; i < wifis.size(); i++) { // concurrent modification is okay, since new conditions are appended at the end
            WifiLocationEntry entry = wifis.get(i);
            List<CellLocationCondition> cellLocationConditions = entry.getCellLocationConditions();

            for (int j = 0; j < cellLocationConditions.size(); j++) { // concurrent modification is okay, since new conditions are appended at the end
                CellLocationCondition condition = cellLocationConditions.get(j);
                if (condition.check(allCells, respectSignalStrength)) {
                    Logger.d(TAG, "Activating Wi-Fi for: " + entry.getSsid());
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
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);
        Pair<Integer, Integer> time = new Pair<>(currentHour, currentMinute);
        Logger.d(TAG, "Number of schedule entries: " + scheduleEntries.size());

        for (int i = 0; i < scheduleEntries.size(); i++) { // concurrent modification is okay, since new conditions are appended at the end
            ScheduleEntry entry = scheduleEntries.get(i);
            if (entry.getScheduleCondition().check(time)) {
                Logger.d(TAG, "Active schedule - " + entry.toString());
                return true; // schedule active, skip the rest
            }
        }

        return false; // no schedule active
    }

}
