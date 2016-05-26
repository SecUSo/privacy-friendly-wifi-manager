package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.view.viewholder.ScheduleItemViewHolder;

import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleItemViewHolder> {
    private Context context;
    private List<ScheduleEntry> scheduleEntries;

    public ScheduleAdapter(Context context, List<ScheduleEntry> scheduleEntries) {
        this.context = context;
        this.scheduleEntries = scheduleEntries;
    }

    @Override
    public ScheduleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_schedule, parent, false);
        return new ScheduleItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ScheduleItemViewHolder holder, int position) {
        holder.setupItem(this.context, this.scheduleEntries.get(position));
    }

    @Override
    public int getItemCount() {
        return this.scheduleEntries.size();
    }
}
