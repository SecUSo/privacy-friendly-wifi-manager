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

package org.secuso.privacyfriendlywifimanager.logic.util;

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
