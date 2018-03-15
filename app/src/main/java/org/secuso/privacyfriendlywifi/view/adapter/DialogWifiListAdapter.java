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

package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.IOnDialogClosedListener;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.view.viewholder.DialogWifiItemViewHolder;

import java.util.List;

import secuso.org.privacyfriendlywifi.R;

/**
 * Adapter for WiFis in a dialog.
 */
public class DialogWifiListAdapter extends RecyclerView.Adapter<DialogWifiItemViewHolder> {
    private final IOnDialogClosedListener listener;
    private List<WifiLocationEntry> knownWifis;

    public DialogWifiListAdapter(List<WifiLocationEntry> knownWifis, IOnDialogClosedListener listener) {
        this.knownWifis = knownWifis;
        this.listener = listener;
    }

    @Override
    public DialogWifiItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(StaticContext.getContext()).inflate(R.layout.list_item_dialog_wifilist, parent, false);
        return new DialogWifiItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DialogWifiItemViewHolder holder, int position) {
        holder.setupItem(this.knownWifis.get(position), this);
    }

    @Override
    public int getItemCount() {
        return this.knownWifis.size();
    }

    public void onListItemClicked(WifiLocationEntry newEntry) {
        this.listener.onDialogClosed(DialogInterface.BUTTON_POSITIVE, newEntry);
    }
}
