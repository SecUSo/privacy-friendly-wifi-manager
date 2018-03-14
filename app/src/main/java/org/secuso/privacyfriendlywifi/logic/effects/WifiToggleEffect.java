/*
 This file is part of Privacy Friendly Wifi Manager.
 Privacy Friendly Wifi Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Wifi Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Wifi Manager. If not, see <http://www.gnu.org/licenses/>.
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
