package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.OnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifi.view.viewholder.WifiItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class WifiListAdapter extends RecyclerView.Adapter<WifiItemViewHolder> implements OnDeleteModeChangedListener, View.OnKeyListener {
    private Context context;
    private List<WifiLocationEntry> wifiLocationEntries;
    private List<WifiItemViewHolder> children;
    private boolean isDeleteModeActive;
    private RecyclerView recyclerView;

    public WifiListAdapter(Context context, List<WifiLocationEntry> wifiLocationEntries, RecyclerView recyclerView) {
        this.context = context;
        this.wifiLocationEntries = wifiLocationEntries;
        this.children = new ArrayList<>();
        this.isDeleteModeActive = false;
        this.recyclerView = recyclerView;
        this.recyclerView.setFocusableInTouchMode(true);
        this.recyclerView.requestFocus();
        this.recyclerView.setOnKeyListener(this);
    }

    @Override
    public WifiItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wifilist, parent, false);
        WifiItemViewHolder newChild = new WifiItemViewHolder(v);
        this.children.add(newChild);
        return newChild;
    }

    @Override
    public void onBindViewHolder(WifiItemViewHolder holder, int position) {
        holder.setupItem(this.context, this.wifiLocationEntries.get(position), this, this);
    }

    @Override
    public void onViewRecycled(WifiItemViewHolder holder) {
        super.onViewRecycled(holder);
        this.children.remove(holder);
    }

    @Override
    public int getItemCount() {
        return this.wifiLocationEntries.size();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (this.isDeleteModeActive() && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            this.setDeleteModeActive(false);
            return true;
        }
        return false;
    }

    @Override
    public void setDeleteModeActive(boolean isActive) {
        this.isDeleteModeActive = isActive;
        for (WifiItemViewHolder vh : this.children) {
            vh.setDeleteButtonVisible(isActive);
        }
        // refresh RecyclerView
        this.notifyDataSetChanged();
    }

    @Override
    public boolean isDeleteModeActive() {
        return this.isDeleteModeActive;
    }

    public boolean remove(WifiLocationEntry entry) {
        boolean ret = false;
        int position = this.wifiLocationEntries.indexOf(entry);
        if (position >= 0) {
            ret = this.wifiLocationEntries.remove(entry);
            // refresh RecyclerView
            this.notifyItemRemoved(position);
            this.notifyItemRangeChanged(position, this.getItemCount());
        }
        return ret;
    }
}
