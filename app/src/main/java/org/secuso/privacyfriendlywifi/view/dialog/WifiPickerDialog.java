package org.secuso.privacyfriendlywifi.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.IOnDialogClosedListener;
import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
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
    private ArrayList<IOnDialogClosedListener> onDialogClosedListeners;
    //    private TextView titleText;
    private AlertDialog alertDialog;

    private List<WifiLocationEntry> managedWifis;

    public WifiPickerDialog() {
        this.onDialogClosedListeners = new ArrayList<>();
    }

    public void show(Activity activity, ViewGroup container) {
        final List<WifiLocationEntry> unknownNetworks = new ArrayList<>();
        DialogWifiListAdapter itemsAdapter = new DialogWifiListAdapter(unknownNetworks, this);

        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_wifi_dialog, container, false);
        // this.titleText = (TextView) view.findViewById(R.id.dialog_title_text);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(activity));
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                progressBar.setVisibility(View.GONE);

                // fetch search results
                WifiHandler.scanAndUpdateWifis(context, unknownNetworks);

                recyclerView.invalidate();
                recyclerView.requestLayout();

                // unregister this receiver
                try {
                    context.unregisterReceiver(this);
                } catch (IllegalArgumentException e) {
                    Logger.d("TAG", "not registered");
                }
            }
        };

        try {
            StaticContext.getContext().unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            Logger.d("TAG", "not registered");
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        StaticContext.getContext().registerReceiver(receiver, filter);

        WifiManager wifiMan = (WifiManager) StaticContext.getContext().getSystemService(Context.WIFI_SERVICE);
        wifiMan.startScan();

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(activity);
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