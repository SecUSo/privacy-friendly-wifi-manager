package org.secuso.privacyfriendlywifi.view.dialog;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private List<WifiLocationEntry> managedWifis;

    public WifiPickerDialog(Context context) {
        this.onDialogClosedListeners = new ArrayList<>();
        this.context = context;
    }

    public void show() {
        View view = LayoutInflater.from(this.context).inflate(R.layout.fragment_wifi_dialog, null, false);

        // this.titleText = (TextView) view.findViewById(R.id.dialog_title_text);

        // get known wifi networks and setup recycler view
        WifiManager wifiMan = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        final List<WifiLocationEntry> unknownNetworks = new ArrayList<>();


        wifiMan.startScan();

        IntentFilter i = new IntentFilter();
        i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                WifiManager w = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                Log.d("TAG", "HANDLER");
                List<ScanResult> l = w.getScanResults();

                for (ScanResult config : l) {
                    boolean found = false;
                    String confSSID = config.SSID;
                    if (confSSID.startsWith("\"") && confSSID.endsWith("\"")) {
                        confSSID = confSSID.substring(1, confSSID.length() - 1);
                    }
                    for (WifiLocationEntry entry : managedWifis) {
                        if (entry.getSsid().equals(confSSID)) {
                            found = true;
                            break;
                        }
                    }


                    Log.i("TAG", "Found unmanaged wifi: " + confSSID);
                    if (!found) { // BSSID=null && entry.getBssid() == config.BSSID) {
                        unknownNetworks.add(new WifiLocationEntry(confSSID, config.BSSID));
                    }
                }

                // context.unregisterReceiver(this);

                recyclerView.invalidate();
                recyclerView.requestLayout();
            }
        };

        try

        {
            context.unregisterReceiver(receiver);
        } catch (
                IllegalArgumentException e
                )

        {
            Log.d("TAG", "not registered");
        }

        context.registerReceiver(receiver, i);

        /*for (WifiLocationEntry entry : this.managedWifis) {
            for (WifiConfiguration config : unknownNetworks) {
                String confSSID = config.SSID;
                if (confSSID.startsWith("\"") && confSSID.endsWith("\"")) {
                    confSSID = confSSID.substring(1, confSSID.length() - 1);
                }
                Log.i("TAG", "entry: " + entry.getSsid() + " - conf: " + confSSID);
                if (entry.getSsid().equals(confSSID)) { // BSSID=null && entry.getBssid() == config.BSSID) {
                    unknownNetworks.remove(config);
                    break;
                }
            }
        }*/

        DialogWifiListAdapter itemsAdapter = new DialogWifiListAdapter(this.context, unknownNetworks, this);


        recyclerView.addItemDecoration(new

                DividerItemDecoration(this.context)

        );
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new

                LinearLayoutManager(this.context)

        );

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)

        {
            builder = new AlertDialog.Builder(this.context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else

        {
            builder = new AlertDialog.Builder(this.context);
        }

        builder.setNegativeButton(R.string.wifi_picker_dialog_button_cancel, null);
        builder.setTitle(R.string.wifi_picker_dialog_title);
        builder.setView(view);

        this.alertDialog = builder.create();
        this.alertDialog.setCancelable(true);
        this.alertDialog.setCanceledOnTouchOutside(true);
        this.alertDialog.setOnCancelListener(this);
        this.alertDialog.setOnShowListener(new DialogInterface.OnShowListener()

                                           {

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
        this.alertDialog.dismiss();
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