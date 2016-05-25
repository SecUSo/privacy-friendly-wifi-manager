package org.secuso.privacyfriendlywifi.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import secuso.org.privacyfriendlywifi.R;


/**
 * Created by dennis on 25.05.16.
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        // Set substring in actionbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.activity_about);
        return rootView;
    }

}
