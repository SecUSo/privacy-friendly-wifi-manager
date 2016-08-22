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
