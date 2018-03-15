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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifimanager.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.IListHandler;
import org.secuso.privacyfriendlywifimanager.view.viewholder.WifiItemViewHolder;

import secuso.org.privacyfriendlywifi.R;

/**
 * Adapter for WifiLocationEntry.
 */
public class WifiListAdapter extends RemovableRecyclerViewAdapter<WifiLocationEntry> {

    public WifiListAdapter(int viewItemLayoutId, IListHandler<WifiLocationEntry> wifiListHandler, RecyclerView recyclerView, FloatingActionButton fab) {
        super(viewItemLayoutId, wifiListHandler, recyclerView, fab);
    }

    @Override
    public WifiItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wifilist, parent, false);
        return new WifiItemViewHolder(v);
    }
}
