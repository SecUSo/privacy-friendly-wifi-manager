package secuso.org.privacyfriendlywifi.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import secuso.org.privacyfriendlywifi.logic.preconditions.WifiLocationCondition;

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

            // testing here, TODO: remove
            WifiLocationCondition test = new WifiLocationCondition();
            test.check(this);
        } finally {
            // tell everyone that we are done
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
