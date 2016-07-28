package org.secuso.privacyfriendlywifi.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.secuso.privacyfriendlywifi.logic.util.Logger;

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

        Button versionTextView = (Button) rootView.findViewById(R.id.buttonDeleteLogfile);

        versionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Logger.deleteLogFile()) {
                    Toast.makeText(getContext(), R.string.info_logfile_deleted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.info_logfile_deletion_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }
}
