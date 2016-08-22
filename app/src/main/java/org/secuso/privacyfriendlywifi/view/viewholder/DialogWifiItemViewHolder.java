package org.secuso.privacyfriendlywifi.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.view.adapter.DialogWifiListAdapter;

import secuso.org.privacyfriendlywifi.R;

/**
 * ItemViewHolder for Wi-Fis in a dialog.
 */
public class DialogWifiItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView ssidTextView;
    private WifiLocationEntry conf;
    private DialogWifiListAdapter adapter;

    /**
     * Constructs a new DialogWifiItemViewHolder.
     * @param itemView A parent view.
     */
    public DialogWifiItemViewHolder(View itemView) {
        super(itemView);
        this.ssidTextView = (TextView) itemView.findViewById(R.id.wifi_ssid);
        itemView.setOnClickListener(this);
    }

    /**
     * Updates the view.
     * @param conf A WifiLocationEntry for the item.
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