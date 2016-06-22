package org.secuso.privacyfriendlywifi.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.FileHandler;
import org.secuso.privacyfriendlywifi.logic.util.IOnDialogClosedListener;
import org.secuso.privacyfriendlywifi.logic.util.ScreenHandler;
import org.secuso.privacyfriendlywifi.service.ManagerService;
import org.secuso.privacyfriendlywifi.view.adapter.WifiListAdapter;
import org.secuso.privacyfriendlywifi.view.decoration.DividerItemDecoration;
import org.secuso.privacyfriendlywifi.view.dialog.WifiPickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class WifiListFragment extends Fragment implements IOnDialogClosedListener {

    private List<WifiLocationEntry> wifiLocationEntries;
    private IOnDialogClosedListener thisClass;

    private RecyclerView recyclerView;
    private WifiListAdapter itemsAdapter;

    public WifiListFragment() {
        // Required empty public constructor
        thisClass = this;
    }

    public static WifiListFragment newInstance() {
        WifiListFragment fragment = new WifiListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.wifiLocationEntries = (List<WifiLocationEntry>) FileHandler.loadObject(context, ManagerService.FN_LOCATION_ENTRIES, false);
        } catch (IOException e) {
            this.wifiLocationEntries = new ArrayList<>();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_wifilist, container, false);

        // Set substring in actionbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.fragment_wifilist);

        // setup the floating action button
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final WifiManager wifiMan = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
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
                        WifiPickerDialog dialog = new WifiPickerDialog(getContext());
                        dialog.addOnDialogClosedListener(thisClass);
                        dialog.setManagedWifis(wifiLocationEntries);
                        dialog.show();
                    }
                }
            });
        }

        // setup recycler view
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getBaseContext()));

        this.itemsAdapter = new WifiListAdapter(getActivity().getBaseContext(), R.layout.list_item_wifilist, this.wifiLocationEntries, this.recyclerView, fab);
        this.recyclerView.setAdapter(this.itemsAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        this.recyclerView.setPadding(
                this.recyclerView.getPaddingLeft(),
                this.recyclerView.getPaddingTop(),
                this.recyclerView.getPaddingRight(),
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                        ScreenHandler.getPXFromDP(fab.getPaddingTop() + fab.getHeight() + fab.getPaddingBottom(), this.getContext())
                        : fab.getPaddingTop() + fab.getHeight() + fab.getPaddingBottom()));

        return rootView;
    }

    @Override
    public void onDialogClosed(int returnCode, Object... returnValue) {
        if (returnCode == DialogInterface.BUTTON_POSITIVE) {
            this.wifiLocationEntries.add((WifiLocationEntry) returnValue[0]);
            this.saveWifiLocationEntries();
            this.recyclerView.requestLayout();
            this.recyclerView.invalidate();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        this.saveWifiLocationEntries();
    }

    private boolean saveWifiLocationEntries() {
        try {
            return FileHandler.storeObject(this.getActivity(), ManagerService.FN_LOCATION_ENTRIES, this.wifiLocationEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}