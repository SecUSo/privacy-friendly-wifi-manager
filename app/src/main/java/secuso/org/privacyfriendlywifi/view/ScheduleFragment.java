package secuso.org.privacyfriendlywifi.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;
import secuso.org.privacyfriendlywifi.service.Controller;
import secuso.org.privacyfriendlywifi.view.adapter.ScheduleAdapter;

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

        // setup example list
        RecyclerView listView = (RecyclerView) rootView.findViewById(R.id.timeslotRecycleView);


        // example list
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add("TimeSlot Nr. " + i);
            items.add("From To Nr. " + i);
        }
        String[] itemsArr = new String[items.size()];
        itemsArr = items.toArray(itemsArr);

        ScheduleAdapter itemsAdapter = new ScheduleAdapter(getActivity(), R.layout.list_item_schedule, itemsArr);
        listView.setAdapter(itemsAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }
}