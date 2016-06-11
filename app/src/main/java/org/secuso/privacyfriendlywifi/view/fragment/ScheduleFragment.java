package org.secuso.privacyfriendlywifi.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.util.FileHandler;
import org.secuso.privacyfriendlywifi.logic.util.OnDialogClosedListener;
import org.secuso.privacyfriendlywifi.logic.util.ScreenHandler;
import org.secuso.privacyfriendlywifi.service.ManagerService;
import org.secuso.privacyfriendlywifi.view.adapter.ScheduleAdapter;
import org.secuso.privacyfriendlywifi.view.decoration.DividerItemDecoration;
import org.secuso.privacyfriendlywifi.view.dialog.TimePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class ScheduleFragment extends Fragment implements OnDialogClosedListener {

    private List<ScheduleEntry> scheduleEntries;
    private OnDialogClosedListener thisClass;

    private RecyclerView recyclerView;

    public ScheduleFragment() {
        // Required empty public constructor
        thisClass = this;
    }

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.scheduleEntries = (List<ScheduleEntry>) FileHandler.loadObject(context, ManagerService.FN_SCHEDULE_ENTRIES, false);
        } catch (IOException e) {
            this.scheduleEntries = new ArrayList<>();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                    TimePickerDialog dialog = new TimePickerDialog(getContext());
                    dialog.addOnDialogClosedListener(thisClass);
                    dialog.setCurrentListSize(scheduleEntries.size());
                    dialog.show();
                }
            });
        }

        // setup recycler view

        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getBaseContext()));

        ScheduleAdapter itemsAdapter = new ScheduleAdapter(getActivity().getBaseContext(), this.scheduleEntries, this.recyclerView, fab);
        this.recyclerView.setAdapter(itemsAdapter);
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
            this.scheduleEntries.add((ScheduleEntry) returnValue[0]);
            this.saveSchedule();
            int currentItemCount = this.recyclerView.getAdapter().getItemCount();
            this.recyclerView.getAdapter().notifyItemInserted(currentItemCount - 1);
            this.recyclerView.getAdapter().notifyItemRangeChanged(currentItemCount - 1, currentItemCount);
            //this.recyclerView.requestLayout();
            //this.recyclerView.invalidate();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        this.saveSchedule();
    }

    private boolean saveSchedule() {
        //TODO sometimes list is not loaded correctly (alternating switch statuses)
        try {
            return FileHandler.storeObject(getActivity(), ManagerService.FN_SCHEDULE_ENTRIES, this.scheduleEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}