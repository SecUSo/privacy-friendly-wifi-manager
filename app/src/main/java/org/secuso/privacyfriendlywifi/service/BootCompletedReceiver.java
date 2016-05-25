package org.secuso.privacyfriendlywifi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO Check Receiver working !!!
        Log.i("TAG", "BOOT COMPLETE WIFI");
        Controller.registerReceivers(context);
    }
}
