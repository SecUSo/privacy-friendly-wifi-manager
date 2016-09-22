package org.secuso.privacyfriendlywifi.view.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.IListHandler;
import org.secuso.privacyfriendlywifi.view.viewholder.WifiItemViewHolder;

import secuso.org.privacyfriendlywifi.R;

/**
 * Adapter for WifiLocationEntry.
 */
public class WifiListAdapter extends RemovableRecyclerViewAdapter<WifiLocationEntry> {

    public WifiListAdapter(int viewItemLayoutId, IListHandler<WifiLocationEntry> wifiListHandler, RecyclerView recyclerView, FloatingActionButton fab) {
        super(viewItemLayoutId, wifiListHandler, recyclerView, fab);
    }

    @Override
    public WifiItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wifilist, parent, false);
        return new WifiItemViewHolder(v);
    }
}
