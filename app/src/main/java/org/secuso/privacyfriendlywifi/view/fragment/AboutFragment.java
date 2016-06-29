package org.secuso.privacyfriendlywifi.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.util.StaticContext;

import secuso.org.privacyfriendlywifi.BuildConfig;
import secuso.org.privacyfriendlywifi.R;


/**
 *
 */


public class AboutFragment extends Fragment {


    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        // Set substring in actionbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.fragment_about);

        TextView versionTextView = (TextView) rootView.findViewById(R.id.about_version);
        String versionText = getString(R.string.about_version);
        versionTextView.setText(String.format(versionText, BuildConfig.VERSION_NAME));

        return rootView;
    }

}
