/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.view.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifimanager.logic.util.IListHandler;
import org.secuso.privacyfriendlywifimanager.logic.util.IOnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifimanager.logic.util.StaticContext;
import org.secuso.privacyfriendlywifimanager.view.viewholder.RemovableItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for lists with deletable items.
 */
public class RemovableRecyclerViewAdapter<EntryType> extends RecyclerView.Adapter<RemovableItemViewHolder<EntryType>> implements IOnDeleteModeChangedListener, View.OnKeyListener {
    private List<RemovableItemViewHolder<EntryType>> children;
    private boolean isDeleteModeActive;
    private FloatingActionButton fab;
    private int viewLayoutId;
    private IListHandler<EntryType> listHandler;

    public RemovableRecyclerViewAdapter(int viewLayoutId, IListHandler<EntryType> listHandler, final RecyclerView recyclerView, FloatingActionButton fab) {
        this.viewLayoutId = viewLayoutId;
        this.children = new ArrayList<>();
        this.isDeleteModeActive = false;
        recyclerView.setFocusableInTouchMode(true);
        recyclerView.requestFocus();
        recyclerView.setOnKeyListener(this);
        this.fab = fab;
        this.listHandler = listHandler;

        recyclerView.setOnTouchListener(new RecyclerView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View child = recyclerView.findChildViewUnder(event.getX(), event.getY());

                if (event.getAction() == MotionEvent.ACTION_DOWN && child == null) {
                    setDeleteModeActive(false);
                }

                return false;
            }
        });
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
        holder.setupItem(StaticContext.getContext(), this.listHandler.get(position), this, this);
    }

    @Override
    public void onViewRecycled(RemovableItemViewHolder<EntryType> holder) {
        super.onViewRecycled(holder);
        this.children.remove(holder);
    }

    @Override
    public int getItemCount() {
        return this.listHandler.size();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (this.isDeleteModeActive() && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            this.setDeleteModeActive(false);
            return true;
        }

        return false;
    }

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

    public boolean isDeleteModeActive() {
        return this.isDeleteModeActive;
    }

    public boolean remove(EntryType recyclerViewEntry) {
        boolean ret = false;
        int position = this.listHandler.indexOf(recyclerViewEntry);

        if (position >= 0) {
            ret = this.listHandler.remove(recyclerViewEntry);

            // refresh RecyclerView
            this.notifyItemRemoved(position);
            this.notifyItemRangeChanged(position, this.getItemCount());
        }

        if (this.listHandler.isEmpty()) {
            this.setDeleteModeActive(false);
        }

        return ret;
    }
}
