package org.secuso.privacyfriendlywifi.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.OnDialogClosedListener;
import org.secuso.privacyfriendlywifi.view.adapter.DialogWifiListAdapter;
import org.secuso.privacyfriendlywifi.view.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class WifiPickerDialog implements OnDialogClosedListener, DialogInterface.OnCancelListener {
    private final Context context;
    private ArrayList<OnDialogClosedListener> onDialogClosedListeners;
    //    private TextView titleText;
    private AlertDialog alertDialog;

    private RecyclerView recyclerView;
    private List<WifiLocationEntry> managedWifis;
    private List<WifiConfiguration> configuredNetworks;

    public WifiPickerDialog(Context context) {
        this.onDialogClosedListeners = new ArrayList<>();
        this.context = context;
    }

    public void show() {
        View view = LayoutInflater.from(this.context).inflate(R.layout.fragment_wifi_dialog, null, false);

//        this.titleText = (TextView) view.findViewById(R.id.dialog_title_text);

        // get known wifi networks and setup recycler view
        WifiManager wifiMan = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        this.configuredNetworks = wifiMan.getConfiguredNetworks();
        if (this.configuredNetworks == null) {
            this.configuredNetworks = new ArrayList<>();
        }

        for (WifiLocationEntry entry : this.managedWifis) {
            for (WifiConfiguration config : this.configuredNetworks) {
                if (entry.getSsid() == config.SSID && entry.getBssid() == config.BSSID) {
                    this.configuredNetworks.remove(config);
                    break;
                }
            }
        }

        final DialogWifiListAdapter itemsAdapter = new DialogWifiListAdapter(this.context, this.configuredNetworks);

        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.context));
        this.recyclerView.setAdapter(itemsAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        this.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiConfiguration conf = configuredNetworks.get(recyclerView.getChildLayoutPosition(v));
                onDialogClosed(DialogInterface.BUTTON_POSITIVE, new WifiLocationEntry(conf.SSID, conf.BSSID));
                alertDialog.dismiss();
            }
        });

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this.context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this.context);
        }
        builder.setNegativeButton(R.string.wifi_picker_dialog_button_cancel, null);
        builder.setTitle(R.string.wifi_picker_dialog_title);
        builder.setView(view);

        this.alertDialog = builder.create();
        this.alertDialog.setCancelable(true);
        this.alertDialog.setCanceledOnTouchOutside(true);
        this.alertDialog.setOnCancelListener(this);
        this.alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                               @Override
                                               public void onShow(DialogInterface dialog) {
                                                   alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                                                           new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   alertDialog.cancel();
                                                               }
                                                           });
                                               }
                                           }

        );

        alertDialog.show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.alertDialog.dismiss();
        this.onDialogClosed(DialogInterface.BUTTON_NEGATIVE);
    }

    @Override
    public void onDialogClosed(int returnCode, Object... returnValue) {
        for (OnDialogClosedListener listener : onDialogClosedListeners) {
            listener.onDialogClosed(returnCode, returnValue);
        }
    }

    public boolean addOnDialogClosedListener(OnDialogClosedListener listener) {
        return this.onDialogClosedListeners.add(listener);
    }

    public boolean removeOnDialogClosedListener(OnDialogClosedListener listener) {
        return this.onDialogClosedListeners.remove(listener);
    }

    public void setManagedWifis(List<WifiLocationEntry> managedWifis) {
        this.managedWifis = managedWifis;
    }
}