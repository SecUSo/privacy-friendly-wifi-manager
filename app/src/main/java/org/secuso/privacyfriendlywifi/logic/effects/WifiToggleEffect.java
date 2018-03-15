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

package org.secuso.privacyfriendlywifi.logic.effects;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;

/**
 * (De-) Activate WiFi.
 */
public class WifiToggleEffect implements IEffect {
    static final String TAG = "WifiToggleEffect";

    @Override
    public void apply(boolean state) {
        if (WifiHandler.hasWifiPermission(StaticContext.getContext())) {
            WifiManager wifiManager = (WifiManager) StaticContext.getContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(state);
        } else {
            Logger.e(TAG, "No wifi permission granted"); // TODO reminder notification
        }
    }
}
