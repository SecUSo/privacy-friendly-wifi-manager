package org.secuso.privacyfriendlywifi.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.logic.util.WifiHandler;
import org.secuso.privacyfriendlywifi.service.ManagerService;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class SettingsFragment extends Fragment {

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

        final Switch signalStrengthSwitch = (Switch) rootView.findViewById(R.id.switchSignalStrength);
        signalStrengthSwitch.setChecked(ManagerService.shouldRespectSignalStrength());

        signalStrengthSwitch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences settings = StaticContext.getContext().getSharedPreferences(ManagerService.PREF_SETTINGS, Context.MODE_PRIVATE);
                        settings.edit().putBoolean(ManagerService.PREF_ENTRY_USE_SIGNAL_STRENGTH, signalStrengthSwitch.isChecked()).apply();
                    }
                }
        );

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
                    Toast.makeText(getContext(), R.string.info_logfile_deleted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.info_logfile_deletion_error, Toast.LENGTH_LONG).show();
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

        Button buttonCheckWifiConnection = (Button) rootView.findViewById(R.id.buttonCheckWifiConnected);

        buttonCheckWifiConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),
                        String.format("Wifi status: enabled: %1$b, connected %2$b",
                                WifiHandler.isWifiEnabled(getContext()),
                                WifiHandler.isWifiConnected(getContext())),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
