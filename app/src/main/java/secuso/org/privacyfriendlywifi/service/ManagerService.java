package secuso.org.privacyfriendlywifi.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

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
        } finally {

            // tell everyone that we are done
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }


}
