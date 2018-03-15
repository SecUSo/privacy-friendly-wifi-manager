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

package org.secuso.privacyfriendlywifi.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.AbstractSettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.IOnDialogClosedListener;
import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.WifiListHandler;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;
import org.secuso.privacyfriendlywifi.view.MainActivity;
import org.secuso.privacyfriendlywifi.view.adapter.WifiListAdapter;
import org.secuso.privacyfriendlywifi.view.decoration.DividerItemDecoration;
import org.secuso.privacyfriendlywifi.view.dialog.WifiPickerDialog;

import java.util.Observable;
import java.util.Observer;

import secuso.org.privacyfriendlywifi.R;

/**
 * Fragment to manage location-based Wi-Fi toggle.
 */
public class WifiListFragment extends Fragment implements IOnDialogClosedListener, Observer {
    private static final String TAG = WifiListFragment.class.getSimpleName();
    private IOnDialogClosedListener thisClass;
    private WifiListHandler wifiListHandler;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private View rootView;

    protected FragmentActivity mActivity;

    public WifiListFragment() {
        // Required empty public constructor
        this.thisClass = this;
    }

    public static WifiListFragment newInstance() {
        WifiListFragment fragment = new WifiListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_wifilist, container, false);

        // Set substring in actionbar
        ActionBar actionBar = ((AppCompatActivity) this.mActivity).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setSubtitle(R.string.fragment_wifilist);
        }

        // setup the floating action button
        this.fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        if (this.fab != null) {
            this.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final WifiManager wifiMan = (WifiManager) StaticContext.getContext().getSystemService(Context.WIFI_SERVICE);
                    if (!wifiMan.isWifiEnabled()) {
                        Snackbar.make(view, R.string.wifi_enable_wifi_to_scan, Snackbar.LENGTH_LONG)
                                .setAction(R.string.wifi_enable_wifi, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                wifiMan.setWifiEnabled(true);
                                                fab.callOnClick();
                                            }
                                        }
                                ).show();
                    } else {
                        WifiPickerDialog dialog = new WifiPickerDialog();
                        dialog.addOnDialogClosedListener(thisClass);
                        dialog.show(mActivity, container);
                    }
                }
            });
        }

        // setup recycler view
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        WifiListAdapter itemsAdapter = new WifiListAdapter(R.layout.list_item_wifilist, this.wifiListHandler, this.recyclerView, fab);

        this.recyclerView.setAdapter(itemsAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        this.wifiListHandler.addObserver(this);

        if (!AbstractSettingsEntry.hasCoarseLocationPermission(getContext())) {
            this.fab.setEnabled(false);
            updateFab(false);
            Snackbar snackbar = Snackbar
                    .make(rootView, R.string.help_desc_coarse_location, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.settings_grant_permissions, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Re-grant permissions
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_WIFI_STATE,
                                            Manifest.permission.CHANGE_WIFI_STATE,
                                            Manifest.permission.ACCESS_COARSE_LOCATION}, MainActivity.DYN_PERMISSION);
                        }
                    });

            // allow longer strings in snackbar
            TextView snackbarTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            snackbarTextView.setMaxLines(999);

            snackbar.show();
        }

        return rootView;
    }

    /**
     * Hides the floating action button to add a new WiFi if permission not granted.
     */
    public void updateFab() {
        Context context = StaticContext.getContext();
        if (context != null) {
            updateFab(AbstractSettingsEntry.hasCoarseLocationPermission(context));
        }
    }

    /**
     * Hides the floating action button to add a new WiFi depending on parameter.
     *
     * @param enabled Visible if true, hidden otherwise.
     */
    private void updateFab(boolean enabled) {
        if (fab != null) {
            fab.setEnabled(enabled);
            fab.setVisibility(enabled ? View.VISIBLE : View.GONE);
            fab.invalidate();
            rootView.invalidate();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        StaticContext.setContext(context);

        this.mActivity = getActivity();

        this.wifiListHandler = new WifiListHandler(context);
        this.wifiListHandler.sort(); // important here: we are sorting before we are waiting for changes since we are creating the list later anyway
    }

    @Override
    public void onDialogClosed(int returnCode, Object... returnValue) {
        if (returnCode == DialogInterface.BUTTON_POSITIVE) {
            this.wifiListHandler.add((WifiLocationEntry) returnValue[0]);
            this.recyclerView.getAdapter().notifyDataSetChanged();

            if (SettingsEntry.isServiceActive(StaticContext.getContext())) {
                AlarmReceiver.fireAndSchedule(StaticContext.getContext());
            }
        }
    }

    @Override
    public void update(Observable o, Object data) {
        if (this.mActivity != null) {
            this.mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!((WifiListAdapter) recyclerView.getAdapter()).isDeleteModeActive()) {
                        recyclerView.swapAdapter(new WifiListAdapter(R.layout.list_item_wifilist, wifiListHandler, recyclerView, fab), false);
                    }

                    recyclerView.requestLayout();
                    recyclerView.invalidate();
                }
            });
        }
    }


}