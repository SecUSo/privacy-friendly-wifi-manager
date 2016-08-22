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
