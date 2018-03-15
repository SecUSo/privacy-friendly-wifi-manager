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
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlywifimanager.logic.preconditions.CellLocationCondition;
import org.secuso.privacyfriendlywifimanager.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.IOnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifimanager.view.DetailsActivity;
import org.secuso.privacyfriendlywifimanager.view.adapter.RemovableRecyclerViewAdapter;

import java.util.Locale;

import secuso.org.privacyfriendlywifi.R;

/**
 * ItemViewHolder for WifiLocationEntry.
 */
public class WifiItemViewHolder extends RemovableItemViewHolder<WifiLocationEntry> {
    private TextView ssidTextView;
    private TextView bssidTextView;
    private TextView numCellsTextView;

    private Context context;
    private WifiLocationEntry entry;

    public WifiItemViewHolder(View itemView) {
        super(itemView);

        this.ssidTextView = (TextView) itemView.findViewById(R.id.wifi_ssid);
        this.bssidTextView = (TextView) itemView.findViewById(R.id.wifi_bssid);
        this.numCellsTextView = (TextView) itemView.findViewById(R.id.num_cells);
    }

    public void setupItem(Context context, WifiLocationEntry entry, RemovableRecyclerViewAdapter<WifiLocationEntry> adapter, IOnDeleteModeChangedListener listener) {
        super.setupItem(context, entry, adapter, listener);

        this.context = context;
        this.entry = entry;

        String ssidText = context.getString(R.string.wifi_ssid_text);
        String bssidText = context.getString(R.string.wifi_bssid_text);
        String multiBssidText = context.getString(R.string.wifi_multi_bssid_text);
        String numCellsText = context.getString(R.string.wifi_num_cells_text);

        this.ssidTextView.setText(String.format(Locale.getDefault(), ssidText, "".equals(entry.getSsid().trim()) ? context.getString(R.string.wifi_hidden_wifi_text) : entry.getSsid()));

        // switch text if more than one MAC
        if (entry.getCellLocationConditions().size() > 1) {
            this.bssidTextView.setText(String.format(Locale.getDefault(), multiBssidText, entry.getCellLocationConditions().size()));
        } else {
            this.bssidTextView.setText(String.format(Locale.getDefault(), bssidText, entry.getCellLocationConditions().get(0).getBssid()));
        }

        int numCells = 0;
        for (CellLocationCondition condition : entry.getCellLocationConditions()) {
            numCells += condition.getNumberOfRelatedCells();
        }
        this.numCellsTextView.setText(String.format(Locale.getDefault(), numCellsText, numCells));

    }

    @Override
    public void onClick(View v) {
        if (this.listener.isDeleteModeActive()) {
            this.listener.setDeleteModeActive(false);
        } else {
            Intent startDetailsActivity = new Intent(context, DetailsActivity.class);
            startDetailsActivity.putExtra(WifiLocationEntry.class.getSimpleName(), this.entry);
            startDetailsActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(startDetailsActivity);
        }
    }
}
