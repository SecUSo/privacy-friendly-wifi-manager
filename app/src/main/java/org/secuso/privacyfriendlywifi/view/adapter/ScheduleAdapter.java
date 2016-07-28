package org.secuso.privacyfriendlywifi.view.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.util.IListHandler;
import org.secuso.privacyfriendlywifi.view.viewholder.ScheduleItemViewHolder;

import secuso.org.privacyfriendlywifi.R;

public class ScheduleAdapter extends RemovableRecyclerViewAdapter<ScheduleEntry> {

    public ScheduleAdapter(int layoutResId, IListHandler<ScheduleEntry> scheduleListHandler, RecyclerView recyclerView, FloatingActionButton fab) {
        super(layoutResId, scheduleListHandler, recyclerView, fab);
    }

    @Override
    public ScheduleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_schedule, parent, false);
        return new ScheduleItemViewHolder(v);
    }
}