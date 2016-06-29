package org.secuso.privacyfriendlywifi.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.service.Controller;

/**
 * BroadcastReceiver for BootCompleted event
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        StaticContext.setContext(context);

        //TODO Check Receiver working !!!
        Log.i("TAG", "BOOT COMPLETE WIFI");
        Controller.registerReceivers();
    }
}
