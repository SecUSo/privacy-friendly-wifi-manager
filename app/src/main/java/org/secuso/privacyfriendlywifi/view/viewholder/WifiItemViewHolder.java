package org.secuso.privacyfriendlywifi.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.OnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifi.view.adapter.WifiListAdapter;

import java.util.Locale;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class WifiItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    private ImageButton deleteButton;
    private TextView ssidTextView;
    private TextView bssidTextView;
    private TextView numCellsTextView;

    private WifiLocationEntry wifiLocationEntry;
    private WifiListAdapter adapter;
    private OnDeleteModeChangedListener listener;

    public WifiItemViewHolder(View itemView) {
        super(itemView);
        this.deleteButton = (ImageButton) itemView.findViewById(R.id.button_delete);
        this.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(wifiLocationEntry);
            }
        });
        this.ssidTextView = (TextView) itemView.findViewById(R.id.wifi_ssid);
        this.bssidTextView = (TextView) itemView.findViewById(R.id.wifi_bssid);
        this.numCellsTextView = (TextView) itemView.findViewById(R.id.num_cells);
        itemView.setOnLongClickListener(this);
    }

    public void setupItem(Context context, WifiLocationEntry entry, WifiListAdapter adapter, OnDeleteModeChangedListener listener) {
        String ssidText = context.getString(R.string.wifi_ssid_text);
        String bssidText = context.getString(R.string.wifi_bssid_text);
        String numCellsText = context.getString(R.string.wifi_num_cells_text);

        this.wifiLocationEntry = entry;
        this.adapter = adapter;
        this.listener = listener;

        this.setDeleteButtonVisible(this.adapter.isDeleteModeActive());

        this.ssidTextView.setText(String.format(Locale.getDefault(), ssidText, entry.getSsid()));
        this.bssidTextView.setText(String.format(Locale.getDefault(), bssidText, entry.getBssid()));
        this.numCellsTextView.setText(String.format(Locale.getDefault(), numCellsText, entry.getCellLocationCondition().getNumberOfRelatedCells()));
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
