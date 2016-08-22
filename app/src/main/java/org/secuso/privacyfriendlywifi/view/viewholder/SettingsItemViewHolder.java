package org.secuso.privacyfriendlywifi.view.viewholder;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;

import secuso.org.privacyfriendlywifi.R;

public class SettingsItemViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView desc;
    private CheckBox checkBox;
    private SharedPreferences settings;

    public SettingsItemViewHolder(View itemView) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.settings_name);
        this.desc = (TextView) itemView.findViewById(R.id.settings_desc);
        this.checkBox = (CheckBox) itemView.findViewById(R.id.settings_checkbox);
        this.settings = StaticContext.getContext().getSharedPreferences(SettingsEntry.PREF_SETTINGS, Context.MODE_PRIVATE);

        // treat item click as checkbox click
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.performClick();
            }
        });
    }

    public void setupItem(String name, String desc, final String preference) {
        this.name.setText(name);
        this.desc.setText(desc);
        this.checkBox.setChecked(this.settings.getBoolean(preference, true));
        this.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.edit().putBoolean(preference, checkBox.isChecked()).apply();
            }
        });
    }

    public void setupItem(String name, String desc, View.OnClickListener clickListener) {
        this.name.setText(name);
        this.desc.setText(desc);
        this.itemView.setOnClickListener(clickListener);
        this.checkBox.setVisibility(View.GONE);
    }
}