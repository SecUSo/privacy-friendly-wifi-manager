package org.secuso.privacyfriendlywifi.logic.util;

import android.view.View;

/**
 *
 */
public class ClickSettingsEntry extends AbstractSettingsEntry {
    public View.OnClickListener clickListener;

    public ClickSettingsEntry(String name, String desc, View.OnClickListener clickListener) {
        super(name, desc);
        this.clickListener = clickListener;
    }
}
