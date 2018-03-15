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

package org.secuso.privacyfriendlywifi.service;

import android.content.Context;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;

/**
 * Controller to (un-) register the service.
 */
public class Controller {
    private static final String TAG = Controller.class.getSimpleName();

    public static void registerReceivers(Context context) {
        Controller.unregisterReceivers(context);
        Logger.v(TAG, "Register all receivers.");
        AlarmReceiver.fireAndSchedule(context);
    }

    public static void unregisterReceivers(Context context) {
        Logger.v(TAG, "Unregister all receivers.");
        AlarmReceiver.cancelAlarm(context);
    }
}
