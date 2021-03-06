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

package org.secuso.privacyfriendlywifimanager.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlywifimanager.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.IOnDialogClosedListener;
import org.secuso.privacyfriendlywifimanager.logic.util.ScheduleListHandler;
import org.secuso.privacyfriendlywifimanager.logic.util.ScreenHandler;
import org.secuso.privacyfriendlywifimanager.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.StaticContext;
import org.secuso.privacyfriendlywifimanager.service.receivers.AlarmReceiver;
import org.secuso.privacyfriendlywifimanager.view.adapter.ScheduleAdapter;
import org.secuso.privacyfriendlywifimanager.view.decoration.DividerItemDecoration;
import org.secuso.privacyfriendlywifimanager.view.dialog.TimePickerDialog;

import java.util.Observable;
import java.util.Observer;

import secuso.org.privacyfriendlywifi.R;

/**
 * Fragment to create new time slots for WiFi deactivation.
 */
public class ScheduleFragment extends Fragment implements IOnDialogClosedListener, Observer {
    private IOnDialogClosedListener thisClass;

    private RecyclerView recyclerView;
    private ScheduleListHandler scheduleListHandler;

    protected FragmentActivity mActivity;

    public ScheduleFragment() {
        // Required empty public constructor
        this.thisClass = this;
    }

    /**
     * Creates a new instance of the ScheduleFragment.
     * @return A new ScheduleFragment.
     */
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        StaticContext.setContext(this.getContext());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        this.scheduleListHandler = new ScheduleListHandler(this.getContext());
        this.scheduleListHandler.sort();

        // Set substring in actionbar
        ActionBar actionBar = ((AppCompatActivity) this.mActivity).getSupportActionBar();

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
                    dialog.show(mActivity, container);
                }
            });
        }

        // setup recycler view

        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(StaticContext.getContext()));

        ScheduleAdapter itemsAdapter = new ScheduleAdapter(R.layout.list_item_schedule, this.scheduleListHandler, this.recyclerView, fab);
        this.recyclerView.setAdapter(itemsAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(StaticContext.getContext()));

        int padding_bottom = 0;
        if (fab != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                padding_bottom = ScreenHandler.getPXFromDP(fab.getPaddingTop() + fab.getHeight() + fab.getPaddingBottom(), StaticContext.getContext());
            } else {
                padding_bottom = fab.getPaddingTop() + fab.getHeight() + fab.getPaddingBottom();
            }
        }

        this.recyclerView.setPadding(
                this.recyclerView.getPaddingLeft(),
                this.recyclerView.getPaddingTop(),
                this.recyclerView.getPaddingRight(),
                padding_bottom);

        this.scheduleListHandler.addObserver(this);

        return rootView;
    }

    @Override
    public void onDialogClosed(int returnCode, Object... returnValue) {
        if (returnCode == DialogInterface.BUTTON_POSITIVE) {
            this.scheduleListHandler.add((ScheduleEntry) returnValue[0]);
            this.recyclerView.getAdapter().notifyDataSetChanged(); // this actually is against the purpose of a recyclerView, but we cannot avoid it at the moment

            // run manager service to calculate new next alarm
            if (SettingsEntry.isServiceActive(StaticContext.getContext())) {
                AlarmReceiver.fireAndSchedule(StaticContext.getContext());
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        // update UI first, since it is asynchronous anyway
        if (this.mActivity != null) {
            this.mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.requestLayout();
                    recyclerView.invalidate();
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }
}