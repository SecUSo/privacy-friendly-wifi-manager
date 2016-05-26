package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.view.viewholder.WifiItemViewHolder;

import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class WifiListAdapter extends RecyclerView.Adapter<WifiItemViewHolder> {
    private Context context;
    private List<WifiLocationEntry> wifiLocationEntries;

    public WifiListAdapter(Context context, List<WifiLocationEntry> wifiLocationEntries) {
        this.context = context;
        this.wifiLocationEntries = wifiLocationEntries;
    }

    @Override
    public WifiItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_wifilist, parent, false);
        return new WifiItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WifiItemViewHolder holder, int position) {
        holder.setupItem(this.context, this.wifiLocationEntries.get(position));
    }

    @Override
    public int getItemCount() {
        return this.wifiLocationEntries.size();
    }
}
