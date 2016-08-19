package org.secuso.privacyfriendlywifi.logic.util;

/**
 * Created by Patrick on 19.08.2016.
 */
public class SettingsEntry {
    public String name;
    public String desc;
    public String preference;

    public SettingsEntry(String name, String desc, String preference) {
        this.name = name;
        this.desc = desc;
        this.preference = preference;
    }
}
