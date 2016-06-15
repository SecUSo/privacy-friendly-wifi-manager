package org.secuso.privacyfriendlywifi.view.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.IOnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifi.view.adapter.RemovableRecyclerViewAdapter;

import java.util.Locale;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class WifiItemViewHolder extends RemovableItemViewHolder<WifiLocationEntry> {
    private TextView ssidTextView;
    private TextView bssidTextView;
    private TextView numCellsTextView;

    public WifiItemViewHolder(View itemView) {
        super(itemView);

        this.ssidTextView = (TextView) itemView.findViewById(R.id.wifi_ssid);
        this.bssidTextView = (TextView) itemView.findViewById(R.id.wifi_bssid);
        this.numCellsTextView = (TextView) itemView.findViewById(R.id.num_cells);
    }

    public void setupItem(Context context, WifiLocationEntry entry, RemovableRecyclerViewAdapter<WifiLocationEntry> adapter, IOnDeleteModeChangedListener listener) {
        super.setupItem(context, entry, adapter, listener);

        String ssidText = context.getString(R.string.wifi_ssid_text);
        String bssidText = context.getString(R.string.wifi_bssid_text);
        String numCellsText = context.getString(R.string.wifi_num_cells_text);

        this.ssidTextView.setText(String.format(Locale.getDefault(), ssidText, entry.getSsid()));
        this.bssidTextView.setText(String.format(Locale.getDefault(), bssidText, entry.getBssid()));
        this.numCellsTextView.setText(String.format(Locale.getDefault(), numCellsText, entry.getCellLocationCondition().getNumberOfRelatedCells()));
    }
}
