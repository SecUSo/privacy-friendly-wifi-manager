package org.secuso.privacyfriendlywifi.view.fragment;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.util.IOnDialogClosedListener;
import org.secuso.privacyfriendlywifi.logic.util.ScheduleListHandler;
import org.secuso.privacyfriendlywifi.logic.util.ScreenHandler;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.view.adapter.ScheduleAdapter;
import org.secuso.privacyfriendlywifi.view.decoration.DividerItemDecoration;
import org.secuso.privacyfriendlywifi.view.dialog.TimePickerDialog;

import java.util.Observable;
import java.util.Observer;

import secuso.org.privacyfriendlywifi.R;

public class ScheduleFragment extends Fragment implements IOnDialogClosedListener, Observer {
    private IOnDialogClosedListener thisClass;

    private RecyclerView recyclerView;
    private ScheduleAdapter itemsAdapter;
    private ScheduleListHandler scheduleListHandler;

    public ScheduleFragment() {
        // Required empty public constructor
        this.thisClass = this;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        StaticContext.setContext(this.getContext());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        this.scheduleListHandler = new ScheduleListHandler();

        // Set substring in actionbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setSubtitle(R.string.fragment_schedule);
        }

        // setup the floating action button
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog dialog = new TimePickerDialog();
                    dialog.addOnDialogClosedListener(thisClass);
                    dialog.setCurrentListSize(scheduleListHandler.size());
                    dialog.show();
                }
            });
        }

        // setup recycler view

        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(StaticContext.getContext()));

        this.itemsAdapter = new ScheduleAdapter(R.layout.list_item_schedule, this.scheduleListHandler, this.recyclerView, fab);
        this.recyclerView.setAdapter(this.itemsAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(StaticContext.getContext()));
        this.recyclerView.setPadding(
                this.recyclerView.getPaddingLeft(),
                this.recyclerView.getPaddingTop(),
                this.recyclerView.getPaddingRight(),
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                        ScreenHandler.getPXFromDP(fab.getPaddingTop() + fab.getHeight() + fab.getPaddingBottom(), StaticContext.getContext())
                        : fab.getPaddingTop() + fab.getHeight() + fab.getPaddingBottom()));

        return rootView;
    }

    @Override
    public void onDialogClosed(int returnCode, Object... returnValue) {
        if (returnCode == DialogInterface.BUTTON_POSITIVE) {
            this.scheduleListHandler.add((ScheduleEntry) returnValue[0]);
            int currentItemCount = this.recyclerView.getAdapter().getItemCount();
            this.recyclerView.getAdapter().notifyItemInserted(currentItemCount - 1);
            this.recyclerView.getAdapter().notifyItemRangeChanged(currentItemCount - 1, currentItemCount);
        }
    }

    public void update(Observable observable, Object data) {
        this.recyclerView.requestLayout();
        this.recyclerView.invalidate();
    }
}