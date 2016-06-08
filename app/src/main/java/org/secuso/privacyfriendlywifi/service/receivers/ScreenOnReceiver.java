package org.secuso.privacyfriendlywifi.service.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 *
 */
public class ScreenOnReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startManagerService = new Intent(context, ManagerService.class);
        startWakefulService(context, startManagerService);
    }
}
