package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.view.viewholder.WifiItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class WifiListAdapter extends RemovableRecyclerViewAdapter<WifiLocationEntry> {
    private List<WifiItemViewHolder> children;

    public WifiListAdapter(Context context, int viewItemLayoutId, List<WifiLocationEntry> wifiLocationEntries, RecyclerView recyclerView, FloatingActionButton fab) {
        super(context, viewItemLayoutId, wifiLocationEntries, recyclerView, fab);
        this.children = new ArrayList<>();
    }

    @Override
    public WifiItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wifilist, parent, false);
        WifiItemViewHolder newChild = new WifiItemViewHolder(v);
        this.children.add(newChild);
        return newChild;
    }
}
