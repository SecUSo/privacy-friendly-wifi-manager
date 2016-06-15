package org.secuso.privacyfriendlywifi.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import org.secuso.privacyfriendlywifi.logic.util.OnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifi.view.adapter.RemovableRecyclerViewAdapter;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class RemovableItemViewHolder<EntryType> extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    private ImageButton deleteButton;
    protected EntryType recyclerViewEntry;
    private RemovableRecyclerViewAdapter<EntryType> adapter;
    private OnDeleteModeChangedListener listener;

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
    }

    public void setupItem(Context context, EntryType recyclerViewEntry, RemovableRecyclerViewAdapter<EntryType> adapter, OnDeleteModeChangedListener listener) {
        this.recyclerViewEntry = recyclerViewEntry;
        this.adapter = adapter;
        this.listener = listener;
        this.setDeleteButtonVisible(this.adapter.isDeleteModeActive());
    }

    @Override
    public boolean onLongClick(View v) {
        this.listener.setDeleteModeActive(true);
        return true;
    }

    public void setDeleteButtonVisible(boolean visible) {
        if (visible) {
            this.deleteButton.setVisibility(View.VISIBLE);
        } else {
            this.deleteButton.setVisibility(View.GONE);
        }
    }
}