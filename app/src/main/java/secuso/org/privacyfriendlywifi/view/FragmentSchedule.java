package secuso.org.privacyfriendlywifi.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;
import secuso.org.privacyfriendlywifi.view.adapter.ScheduleAdapter;

public class FragmentSchedule extends Fragment {

    public FragmentSchedule() {
        // Required empty public constructor
    }

    public static FragmentSchedule newInstance() {
        FragmentSchedule fragment = new FragmentSchedule();
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

        // setup example list
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
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

        return rootView;
    }
}