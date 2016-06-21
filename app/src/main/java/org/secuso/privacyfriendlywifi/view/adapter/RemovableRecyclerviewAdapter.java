package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.util.IOnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifi.view.viewholder.RemovableItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RemovableRecyclerViewAdapter<EntryType> extends RecyclerView.Adapter<RemovableItemViewHolder<EntryType>> implements IOnDeleteModeChangedListener, View.OnKeyListener {
    private Context context;
    private List<EntryType> recyclerViewEntries;
    private List<RemovableItemViewHolder<EntryType>> children;
    private boolean isDeleteModeActive;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private int viewLayoutId;

    public RemovableRecyclerViewAdapter(Context context, int viewLayoutId, List<EntryType> recyclerViewEntries, RecyclerView recyclerView, FloatingActionButton fab) {
        this.context = context;
        this.viewLayoutId = viewLayoutId;
        this.recyclerViewEntries = recyclerViewEntries;
        this.children = new ArrayList<>();
        this.isDeleteModeActive = false;
        this.recyclerView = recyclerView;
        this.recyclerView.setFocusableInTouchMode(true);
        this.recyclerView.requestFocus();
        this.recyclerView.setOnKeyListener(this);
        this.fab = fab;
    }

    @Override
    public RemovableItemViewHolder<EntryType> onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.viewLayoutId, parent, false);
        RemovableItemViewHolder<EntryType> newChild = new RemovableItemViewHolder<>(v);
        this.children.add(newChild);
        return newChild;
    }

    @Override
    public void onBindViewHolder(RemovableItemViewHolder<EntryType> holder, int position) {
        holder.setupItem(this.context, this.recyclerViewEntries.get(position), this, this);
    }

    @Override
    public void onViewRecycled(RemovableItemViewHolder<EntryType> holder) {
        super.onViewRecycled(holder);
        this.children.remove(holder);
    }

    @Override
    public int getItemCount() {
        return this.recyclerViewEntries.size();
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
        for (RemovableItemViewHolder<EntryType> vh : this.children) {
            vh.setDeleteButtonVisible(isActive);
        }
        // refresh RecyclerView
        this.notifyDataSetChanged();

        if (isActive) {
            this.fab.hide();
        } else {
            this.fab.show();
        }
    }

    @Override
    public boolean isDeleteModeActive() {
        return this.isDeleteModeActive;
    }

    public boolean remove(EntryType recyclerViewEntry) {
        boolean ret = false;
        int position = this.recyclerViewEntries.indexOf(recyclerViewEntry);
        if (position >= 0) {
            ret = this.recyclerViewEntries.remove(recyclerViewEntry);
            // refresh RecyclerView
            this.notifyItemRemoved(position);
            this.notifyItemRangeChanged(position, this.getItemCount());
        }
        if (this.recyclerViewEntries.isEmpty()) {
            this.setDeleteModeActive(false);
        }
        return ret;
    }
}
