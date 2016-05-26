package org.secuso.privacyfriendlywifi.view.fragment;

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
import org.secuso.privacyfriendlywifi.view.adapter.WifiListAdapter;
import org.secuso.privacyfriendlywifi.view.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class WifiListFragment extends Fragment {

    public WifiListFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_wifilist, container, false);

        // Set substring in actionbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.fragment_wifilist);

        // setup the floating action button
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your first action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        // setup recycler view
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        // example list
        List<WifiLocationEntry> wifiLocationEntries = new ArrayList<>();
        wifiLocationEntries.add(new WifiLocationEntry("eduroam", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam1", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam2", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam3", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam4", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("eduroam", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam1", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam2", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam3", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam4", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("eduroam", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam1", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam2", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam3", "11:22:33:44:55:66"));
        wifiLocationEntries.add(new WifiLocationEntry("edunotroam4", "11:22:33:44:55:66"));

        WifiListAdapter itemsAdapter = new WifiListAdapter(getContext(), wifiLocationEntries);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    //TODO fade out fab on scroll
}