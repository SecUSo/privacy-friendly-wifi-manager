package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.view.viewholder.ScheduleItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class ScheduleAdapter extends RemovableRecyclerViewAdapter<ScheduleEntry> {
    private List<ScheduleItemViewHolder> children;

    public ScheduleAdapter(Context context, int layoutResId, List<ScheduleEntry> scheduleEntries, RecyclerView recyclerView, FloatingActionButton fab) {
        super(context, layoutResId, scheduleEntries, recyclerView, fab);
        this.children = new ArrayList<>();
    }

    @Override
    public ScheduleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_schedule, parent, false);
        ScheduleItemViewHolder newChild = new ScheduleItemViewHolder(v);
        this.children.add(newChild);
        return newChild;
    }
}