package secuso.org.privacyfriendlywifi.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.logic.types.PrimitiveCellInfo;
import secuso.org.privacyfriendlywifi.logic.types.PrimitiveCellInfoTreeSet;
import secuso.org.privacyfriendlywifi.logic.types.WifiLocationEntry;
import secuso.org.privacyfriendlywifi.logic.util.FileHandler;

/**
 *
 */
public class ManagerService extends IntentService {
    private final String FN_LOCATION_ENTRIES = "fn_location_entries";

    private List<WifiLocationEntry> wifiLocationEntries;

    public ManagerService() {
        super(ManagerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // DO STUFF
            try {
                Object o = FileHandler.loadObject(this, FN_LOCATION_ENTRIES, false);
                if (o instanceof List) {
                    this.wifiLocationEntries = (List<WifiLocationEntry>) List.class.cast(o);
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
