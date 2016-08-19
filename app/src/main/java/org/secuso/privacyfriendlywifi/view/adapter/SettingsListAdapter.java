package org.secuso.privacyfriendlywifi.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.view.viewholder.SettingsItemViewHolder;

import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class SettingsListAdapter extends RecyclerView.Adapter<SettingsItemViewHolder> {
    private List<SettingsEntry> settingsEntryList;

    public SettingsListAdapter(List<SettingsEntry> settingsEntryList) {
        this.settingsEntryList = settingsEntryList;
    }

    @Override
    public SettingsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_settings, parent, false);
        return new SettingsItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SettingsItemViewHolder holder, int position) {
        SettingsEntry entry = this.settingsEntryList.get(position);
        holder.setupItem(entry.name, entry.desc, entry.preference);
    }

    @Override
    public int getItemCount() {
        return this.settingsEntryList.size();
    }
}
