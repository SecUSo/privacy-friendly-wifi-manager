/*
 This file is part of Privacy Friendly Wifi Manager.
 Privacy Friendly Wifi Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Wifi Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Wifi Manager. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlywifi.view.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.util.IListHandler;
import org.secuso.privacyfriendlywifi.logic.util.IOnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.view.viewholder.RemovableItemViewHolder;

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
