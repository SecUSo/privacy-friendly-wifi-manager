package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.view.viewholder.DialogWifiItemViewHolder;

import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class DialogWifiListAdapter extends RecyclerView.Adapter<DialogWifiItemViewHolder> {
    private List<WifiConfiguration> knownWifis;

    public DialogWifiListAdapter(Context context, List<WifiConfiguration> knownWifis) {
        this.knownWifis = knownWifis;
    }

    @Override
    public DialogWifiItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_dialog_wifilist, parent, false);
        return new DialogWifiItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DialogWifiItemViewHolder holder, int position) {
        holder.setupItem(this.knownWifis.get(position));
    }

    @Override
    public int getItemCount() {
        return this.knownWifis.size();
    }
}
