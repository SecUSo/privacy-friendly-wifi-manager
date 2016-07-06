package org.secuso.privacyfriendlywifi.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.service.Controller;

/**
 *  BroadcastReceiver for BootCompleted event
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    private final static String TAG = BootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        StaticContext.setContext(context);

        Logger.i(TAG, "Boot completed");
        Controller.registerReceivers();
    }
}
