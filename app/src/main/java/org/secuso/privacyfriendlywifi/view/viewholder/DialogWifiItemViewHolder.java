package org.secuso.privacyfriendlywifi.view.viewholder;

import android.net.wifi.WifiConfiguration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class DialogWifiItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView ssidTextView;

    public DialogWifiItemViewHolder(View itemView) {
        super(itemView);
        this.ssidTextView = (TextView) itemView.findViewById(R.id.wifi_ssid);
    }

    public void setupItem(WifiConfiguration config) {
        this.ssidTextView.setText(config.SSID);
    }

    @Override
    public void onClick(View v) {
        Log.i("TAG","PINGPINGPING");
    }
}
