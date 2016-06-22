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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import org.secuso.privacyfriendlywifi.logic.preconditions.CellLocationCondition;
import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.IOnDialogClosedListener;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;
import org.secuso.privacyfriendlywifi.view.adapter.DialogWifiListAdapter;
import org.secuso.privacyfriendlywifi.view.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class WifiPickerDialog implements IOnDialogClosedListener, DialogInterface.OnCancelListener {
    private final Context context;
    private ArrayList<IOnDialogClosedListener> onDialogClosedListeners;
    //    private TextView titleText;
    private AlertDialog alertDialog;

    private List<WifiLocationEntry> managedWifis;

    public WifiPickerDialog(Context context) {
        this.onDialogClosedListeners = new ArrayList<>();
        this.context = context;
    }

    public void show() {
        final List<WifiLocationEntry> unknownNetworks = new ArrayList<>();
        DialogWifiListAdapter itemsAdapter = new DialogWifiListAdapter(this.context, unknownNetworks, this);

        View view = LayoutInflater.from(this.context).inflate(R.layout.fragment_wifi_dialog, null, false);
        // this.titleText = (TextView) view.findViewById(R.id.dialog_title_text);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.context));
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                progressBar.setVisibility(View.GONE);
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                List<ScanResult> scanResults = wifiManager.getScanResults();

                for (ScanResult config : scanResults) {
                    String confSSID = WifiHandler.getCleanSSID(config.SSID);

                    boolean alreadyManaged = false;

                    for (WifiLocationEntry entry : managedWifis) {
                        if (confSSID.equals(entry.getSsid())) {
                            // ssid is already present, so we should add the MAC to the existing network
                            entry.addCellLocationCondition(new CellLocationCondition(config.BSSID)); // FIXME: Don't do this here, handle new MACs in own method!
                            alreadyManaged = true;
                            break;
                        }
                    }

                    if (!alreadyManaged) {
                        boolean isNewEntry = true;

                        for (WifiLocationEntry entry : unknownNetworks) {
                            if (confSSID.equals(entry.getSsid())) {
                                entry.addCellLocationCondition(new CellLocationCondition(config.BSSID));
                                isNewEntry = false;
                                break;
                            }
                        }

                        if (isNewEntry) {
                            unknownNetworks.add(new WifiLocationEntry(confSSID, config.BSSID));
                        }
                    }
                }

                recyclerView.invalidate();
                recyclerView.requestLayout();

                // unregister this receiver
                try {
                    context.unregisterReceiver(this);
                } catch (IllegalArgumentException e) {
                    // Log.d("TAG", "not registered");
                }
            }
        };

        try {
            context.unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            // Log.d("TAG", "not registered");
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(receiver, filter);

        WifiManager wifiMan = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        wifiMan.startScan();

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
        });
        this.alertDialog.show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.alertDialog.dismiss();
        this.onDialogClosed(DialogInterface.BUTTON_NEGATIVE);
    }

    @Override
    public void onDialogClosed(int returnCode, Object... returnValue) {
        this.alertDialog.dismiss();
        for (IOnDialogClosedListener listener : onDialogClosedListeners) {
            listener.onDialogClosed(returnCode, returnValue);
        }
    }

    public boolean addOnDialogClosedListener(IOnDialogClosedListener listener) {
        return this.onDialogClosedListeners.add(listener);
    }

    public boolean removeOnDialogClosedListener(IOnDialogClosedListener listener) {
        return this.onDialogClosedListeners.remove(listener);
    }

    public void setManagedWifis(List<WifiLocationEntry> managedWifis) {
        this.managedWifis = managedWifis;
    }
}