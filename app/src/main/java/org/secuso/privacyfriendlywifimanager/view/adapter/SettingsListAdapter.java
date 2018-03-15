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

package org.secuso.privacyfriendlywifimanager.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifimanager.logic.util.AbstractSettingsEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.ClickSettingsEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifimanager.view.viewholder.SettingsItemViewHolder;

import java.util.List;

import secuso.org.privacyfriendlywifi.R;

/**
 * Adapter for Settings.
 */
public class SettingsListAdapter extends RecyclerView.Adapter<SettingsItemViewHolder> {
    private List<AbstractSettingsEntry> settingsEntryList;

    public SettingsListAdapter(List<AbstractSettingsEntry> settingsEntryList) {
        this.settingsEntryList = settingsEntryList;
    }

    @Override
    public SettingsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_settings, parent, false);
        return new SettingsItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SettingsItemViewHolder holder, int position) {
        AbstractSettingsEntry entry = this.settingsEntryList.get(position);
        if (entry instanceof SettingsEntry) {
            holder.setupItem(entry.name, entry.desc, ((SettingsEntry) entry).preference);
        } else {
            holder.setupItem(entry.name, entry.desc, ((ClickSettingsEntry) entry).clickListener);
        }
    }

    @Override
    public int getItemCount() {
        return this.settingsEntryList.size();
    }
}
