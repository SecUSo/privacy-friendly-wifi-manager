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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlywifimanager.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.StaticContext;
import org.secuso.privacyfriendlywifimanager.view.adapter.DialogWifiListAdapter;

import secuso.org.privacyfriendlywifi.R;

/**
 * ItemViewHolder for WiFis in a dialog.
 */
public class DialogWifiItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView ssidTextView;
    private WifiLocationEntry conf;
    private DialogWifiListAdapter adapter;

    /**
     * Constructs a new DialogWifiItemViewHolder.
     *
     * @param itemView A parent view.
     */
    public DialogWifiItemViewHolder(View itemView) {
        super(itemView);
        this.ssidTextView = (TextView) itemView.findViewById(R.id.wifi_ssid);
        itemView.setOnClickListener(this);
    }

    /**
     * Updates the view.
     *
     * @param conf    A WifiLocationEntry for the item.
     * @param adapter A DialogWifiListAdapter for onListItemClicked-Callback.
     */
    public void setupItem(WifiLocationEntry conf, DialogWifiListAdapter adapter) {
        this.conf = conf;
        this.adapter = adapter;
        this.ssidTextView.setText("".equals(conf.getSsid().trim()) ? StaticContext.getContext().getString(R.string.wifi_hidden_wifi_text) : conf.getSsid());
    }

    @Override
    public void onClick(View v) {
        this.adapter.onListItemClicked(this.conf);
    }
}