package secuso.org.privacyfriendlywifi.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import secuso.org.privacyfriendlywifi.logic.preconditions.CellLocationCondition;

/**
 *
 */
public class ManagerService extends IntentService {


    public ManagerService() {
        super(ManagerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // DO STUFF
            // getFilesDir()

            // testing here, TODO: remove
            CellLocationCondition test = new CellLocationCondition();
            test.check(this);
        } finally {
            // tell everyone that we are done
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }
}
