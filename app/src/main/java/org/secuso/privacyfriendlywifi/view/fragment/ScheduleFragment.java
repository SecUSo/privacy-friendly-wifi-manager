package org.secuso.privacyfriendlywifi.view.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.service.Controller;
import org.secuso.privacyfriendlywifi.view.adapter.ScheduleAdapter;
import org.secuso.privacyfriendlywifi.view.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class ScheduleFragment extends Fragment {

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Set substring in actionbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.fragment_schedule);

        // setup the floating action button
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your second action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        // TODO Switch for debugging. Removefor release
        // ### begin switch ###
        Switch asdfg = (Switch) rootView.findViewById(R.id.switch1);
        asdfg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("TAG", "register all the receivers");
                    Controller.registerReceivers(getContext());
                } else {
                    Log.i("TAG", "UNregister all the receivers");
                    Controller.unregisterReceivers(getContext());
                }
            }
        });
        // ### end switch ###

        // setup recycler view
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        List<ScheduleEntry> scheduleEntries = new ArrayList<>();
        scheduleEntries.add(new ScheduleEntry("Test nummer 1", 1, 2, 3, 4));
        scheduleEntries.add(new ScheduleEntry("Test blabla 1", 2, 3, 4, 15));
        scheduleEntries.add(new ScheduleEntry("Test nummer 1", 1, 2, 3, 4));
        scheduleEntries.add(new ScheduleEntry("Test blabla 1", 2, 3, 4, 55));
        scheduleEntries.add(new ScheduleEntry("Test nummer 1", 1, 2, 3, 4));
        scheduleEntries.add(new ScheduleEntry("Test blabla 1", 2, 3, 4, 15));
        scheduleEntries.add(new ScheduleEntry("Test nummer 1", 1, 2, 3, 4));
        scheduleEntries.add(new ScheduleEntry("Test blabla 1", 2, 3, 4, 55));
        scheduleEntries.add(new ScheduleEntry("Test nummer 1", 1, 2, 3, 4));
        scheduleEntries.add(new ScheduleEntry("Test blabla 1", 2, 3, 4, 15));
        scheduleEntries.add(new ScheduleEntry("Test nummer 1", 1, 2, 3, 4));
        scheduleEntries.add(new ScheduleEntry("Test blabla 1", 2, 3, 4, 55));
        scheduleEntries.add(new ScheduleEntry("Test nummer 1", 1, 2, 3, 4));
        scheduleEntries.add(new ScheduleEntry("Test blabla 1", 2, 3, 4, 15));
        scheduleEntries.add(new ScheduleEntry("Test nummer 1", 1, 2, 3, 4));
        scheduleEntries.add(new ScheduleEntry("Test blabla 1", 2, 3, 4, 55));
        scheduleEntries.add(new ScheduleEntry("Test nummer 1", 1, 2, 3, 4));
        scheduleEntries.add(new ScheduleEntry("Test blabla 1", 2, 3, 4, 15));

        ScheduleAdapter itemsAdapter = new ScheduleAdapter(getContext(), scheduleEntries);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    //TODO fade out fab on scroll
}