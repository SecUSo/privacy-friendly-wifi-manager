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

package org.secuso.privacyfriendlywifi.logic.util;

/**
 * This class is used to represent a SharedPreference settings entry in the settings fragment.
 */
public class SettingsEntry extends AbstractSettingsEntry{

    public String preference;

    /**
     * Creates a new SettingsEntry for use with a SettingsItemViewHolder.
     *
     * @param name       Settings name.
     * @param desc       Short description.
     * @param preference Preference string in SharedPreferences storage.
     */
    public SettingsEntry(String name, String desc, String preference) {
        super(name, desc);
        this.preference = preference;
    }
}
