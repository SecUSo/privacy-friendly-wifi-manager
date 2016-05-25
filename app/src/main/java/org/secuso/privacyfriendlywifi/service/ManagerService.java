package org.secuso.privacyfriendlywifi.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfo;
import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfoTreeSet;
import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ManagerService extends IntentService {
    private static final String FN_LOCATION_ENTRIES = "fn_location_entries";
    private static final String FN_SCHEDULE_ENTRIES = "fn_schedule_entries";

    private List<WifiLocationEntry> wifiLocationEntries;

    public ManagerService() {
        super(ManagerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // TODO Check schedule

            // TODO Check Wifi On/Off

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
                entry.getCellLocationCondition().check(this, allCells);
            }

            try {
                FileHandler.storeObject(this, FN_LOCATION_ENTRIES, this.wifiLocationEntries);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            // tell everyone that we are done
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }
}
