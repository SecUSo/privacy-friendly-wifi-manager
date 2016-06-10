package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.util.OnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifi.view.viewholder.ScheduleItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleItemViewHolder> implements OnDeleteModeChangedListener, View.OnKeyListener {
    private Context context;
    private List<ScheduleEntry> scheduleEntries;
    private List<ScheduleItemViewHolder> children;
    private boolean isDeleteModeActive;
    private RecyclerView recyclerView;

    public ScheduleAdapter(Context context, List<ScheduleEntry> scheduleEntries, RecyclerView recyclerView) {
        this.context = context;
        this.scheduleEntries = scheduleEntries;
        this.children = new ArrayList<>();
        this.isDeleteModeActive = false;
        this.recyclerView = recyclerView;
        this.recyclerView.setFocusableInTouchMode(true);
        this.recyclerView.requestFocus();
        this.recyclerView.setOnKeyListener(this);
    }

    @Override
    public ScheduleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_schedule, parent, false);
        ScheduleItemViewHolder newChild = new ScheduleItemViewHolder(v);
        this.children.add(newChild);
        return newChild;
    }

    @Override
    public void onBindViewHolder(ScheduleItemViewHolder holder, int position) {
        holder.setupItem(this.context, this.scheduleEntries.get(position), this, this);
    }

    @Override
    public void onViewRecycled(ScheduleItemViewHolder holder) {
        super.onViewRecycled(holder);
        this.children.remove(holder);
    }

    @Override
    public int getItemCount() {
        return this.scheduleEntries.size();
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.i("TAG", "ONKEYONEKYNEONYKENYOENYEONYEKLNYOE-" + keyCode);
        if (this.isDeleteModeActive() && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            this.setDeleteModeActive(false);
            return true;
        }
        return false;
    }

    @Override
    public void setDeleteModeActive(boolean isActive) {
        this.isDeleteModeActive = isActive;
        for (ScheduleItemViewHolder vh : this.children) {
            vh.setDeleteButtonVisible(isActive);
        }
        // refresh RecyclerView
        this.notifyDataSetChanged();
    }

    @Override
    public boolean isDeleteModeActive() {
        return this.isDeleteModeActive;
    }

    public boolean remove(ScheduleEntry entry) {
        boolean ret = false;
        int position = this.scheduleEntries.indexOf(entry);
        if (position >= 0) {
            ret = this.scheduleEntries.remove(entry);
            // refresh RecyclerView
            this.notifyItemRemoved(position);
            this.notifyItemRangeChanged(position, this.getItemCount());
        }
        return ret;
    }
}