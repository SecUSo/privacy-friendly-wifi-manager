package org.secuso.privacyfriendlywifi.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Set substring in actionbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.fragment_settings);

        return rootView;
    }
}
