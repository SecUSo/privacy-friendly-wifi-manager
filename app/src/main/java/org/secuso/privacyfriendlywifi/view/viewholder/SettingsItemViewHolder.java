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

/**
 * ItemViewHolder for Settings.
 */
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