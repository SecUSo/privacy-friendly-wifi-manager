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

package org.secuso.privacyfriendlywifimanager.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import org.secuso.privacyfriendlywifimanager.logic.util.IOnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifimanager.view.adapter.RemovableRecyclerViewAdapter;

import secuso.org.privacyfriendlywifi.R;

/**
 * An ItemViewHolder for use in removable item lists.
 */
public class RemovableItemViewHolder<EntryType> extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
    private ImageButton deleteButton;
    protected EntryType recyclerViewEntry;
    private RemovableRecyclerViewAdapter<EntryType> adapter;
    protected IOnDeleteModeChangedListener listener;

    /**
     * Constructs a new RemovableItemViewHolder.
     *
     * @param itemView A parent view.
     */
    public RemovableItemViewHolder(View itemView) {
        super(itemView);
        this.deleteButton = (ImageButton) itemView.findViewById(R.id.button_delete);
        this.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(recyclerViewEntry);
            }
        });
        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);
    }

    /**
     * Updates the view.
     *
     * @param context           A context to use.
     * @param recyclerViewEntry The EntryType to represent.
     * @param adapter           An adapter for the delete mode callback.
     * @param listener          A listener for the delete mode callback.
     */
    public void setupItem(Context context, EntryType recyclerViewEntry, RemovableRecyclerViewAdapter<EntryType> adapter, IOnDeleteModeChangedListener listener) {
        this.recyclerViewEntry = recyclerViewEntry;
        this.adapter = adapter;
        this.listener = listener;
        this.setDeleteButtonVisible(this.adapter.isDeleteModeActive());

    }

    @Override
    public boolean onLongClick(View v) {
        this.listener.setDeleteModeActive(!this.listener.isDeleteModeActive());
        return true;
    }

    @Override
    public void onClick(View v) {
        if (this.listener.isDeleteModeActive()) {
            this.listener.setDeleteModeActive(false);
        }
    }

    /**
     * Toggles visibility of delete icon.
     *
     * @param visible Visibility state.
     */
    public void setDeleteButtonVisible(boolean visible) {
        if (visible) {
            this.deleteButton.setVisibility(View.VISIBLE);
        } else {
            this.deleteButton.setVisibility(View.GONE);
        }
    }
}