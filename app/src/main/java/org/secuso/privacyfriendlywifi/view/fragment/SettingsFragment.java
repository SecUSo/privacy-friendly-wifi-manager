package org.secuso.privacyfriendlywifi.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.service.ManagerService;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;
import org.secuso.privacyfriendlywifi.view.adapter.SettingsListAdapter;
import org.secuso.privacyfriendlywifi.view.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class SettingsFragment extends Fragment {
    SharedPreferences settings;
    LinearLayout developerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Set substring in actionbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setSubtitle(R.string.fragment_settings);
        }

        this.settings = StaticContext.getContext().getSharedPreferences(ManagerService.PREF_SETTINGS, Context.MODE_PRIVATE);

         /* GENERAL SETTINGS - START */

        // setup recycler view
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.settingsList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        List<SettingsEntry> settingsEntries = new ArrayList<>();

        /* Add settings here */
        settingsEntries.add(new SettingsEntry(getString(R.string.settings_show_notification_to_add_unmanaged_wi_fis), getString(R.string.settings_show_notification_to_add_unmanaged_wi_fis_desc), ManagerService.PREF_ENTRY_SHOW_NOTIFICATION));
        settingsEntries.add(new SettingsEntry(getString(R.string.settings_respect_signal_strength), getString(R.string.settings_respect_signal_strength_desc), ManagerService.PREF_ENTRY_USE_SIGNAL_STRENGTH));

        recyclerView.setAdapter(new SettingsListAdapter(settingsEntries));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        /* GENERAL SETTINGS - END */

        /* DEVELOPER SETTINGS - START */

        boolean showDeveloper = settings.getBoolean(ManagerService.PREF_ENTRY_DEVELOPER, false);
        this.developerLayout = (LinearLayout) rootView.findViewById(R.id.layout_developer);
        final TextView textViewGeneralSettings = (TextView) rootView.findViewById(R.id.textGeneralSettings);

        final Button buttonHideDeveloper = (Button) rootView.findViewById(R.id.buttonHideDeveloperSettings);

        buttonHideDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.edit().putBoolean(ManagerService.PREF_ENTRY_DEVELOPER, false).apply();
                developerLayout.setVisibility(View.GONE);

                // reset counter
                textViewGeneralSettings.setOnClickListener(new DeveloperClickListener());
            }
        });

        if (showDeveloper) {
            developerLayout.setVisibility(View.VISIBLE);
        } else {
            textViewGeneralSettings.setOnClickListener(new DeveloperClickListener());
        }

        Button buttonTriggerService = (Button) rootView.findViewById(R.id.buttonTriggerService);

        buttonTriggerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmReceiver.fire();
            }
        });

        Button buttonDeleteLogfile = (Button) rootView.findViewById(R.id.buttonDeleteLogfile);

        buttonDeleteLogfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Logger.deleteLogFile()) {
                    Toast.makeText(getContext(), R.string.settings_info_logfile_deleted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.settings_info_logfile_deletion_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        Button buttonFlushLogfile = (Button) rootView.findViewById(R.id.buttonFlushLog);

        buttonFlushLogfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.flush();
            }
        });

        /* DEVELOPER SETTINGS - END */

        return rootView;
    }

    class DeveloperClickListener implements View.OnClickListener {
        int clicked = 0;
        int clicksNeeded = 6;
        Toast infoToast;


        @Override
        public void onClick(View v) {
            clicked++;

            if (clicked >= 3) {
                int leftToDev = clicksNeeded - Math.min(clicked, clicksNeeded);

                if (this.infoToast != null) {
                    this.infoToast.cancel();
                }

                this.infoToast = Toast.makeText(getContext(), String.format(Locale.getDefault(), "%d steps to become a developer.", leftToDev), Toast.LENGTH_SHORT);
                this.infoToast.show();

                if (clicked >= clicksNeeded) {
                    settings.edit().putBoolean(ManagerService.PREF_ENTRY_DEVELOPER, true).apply();
                    developerLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
