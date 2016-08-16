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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.service.ManagerService;
import org.secuso.privacyfriendlywifi.service.receivers.AlarmReceiver;

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

        boolean showDeveleoper = settings.getBoolean(ManagerService.PREF_ENTRY_DEVELOPER, false);
        this.developerLayout = (LinearLayout) rootView.findViewById(R.id.layout_developer);
        final TextView textViewGeneralSettings = (TextView) rootView.findViewById(R.id.textGeneralSettings);

        if (showDeveleoper) {
            developerLayout.setVisibility(View.VISIBLE);

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
        } else {
            textViewGeneralSettings.setOnClickListener(new DeveloperClickListener());
        }

        final Switch signalStrengthSwitch = (Switch) rootView.findViewById(R.id.switchSignalStrength);
        signalStrengthSwitch.setChecked(ManagerService.shouldRespectSignalStrength());

        signalStrengthSwitch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
