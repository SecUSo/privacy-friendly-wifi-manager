/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.secuso.privacyfriendlywifimanager.logic.util.Logger;
import org.secuso.privacyfriendlywifimanager.logic.util.StaticContext;
import org.secuso.privacyfriendlywifimanager.service.Controller;

/**
 *  BroadcastReceiver for BootCompleted event
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    private final static String TAG = BootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        StaticContext.setContext(context);

        Logger.i(TAG, "Boot completed");
        Controller.registerReceivers(context);
    }
}
