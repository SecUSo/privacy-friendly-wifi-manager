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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.IListHandler;
import org.secuso.privacyfriendlywifi.view.viewholder.WifiItemViewHolder;

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
