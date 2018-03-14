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

package org.secuso.privacyfriendlywifi.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.util.AbstractSettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.ClickSettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.view.viewholder.SettingsItemViewHolder;

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
