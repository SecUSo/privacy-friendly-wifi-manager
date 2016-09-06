package org.secuso.privacyfriendlywifi.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import secuso.org.privacyfriendlywifi.R;

/**
 * Help fragment used in ViewPagerActivity.
 */
public class TutorialFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial_step1, container, false);
    }

    public static TutorialFragment1 newInstance() {

        TutorialFragment1 f = new TutorialFragment1();
        Bundle b = new Bundle();
        f.setArguments(b);

        return f;
    }
}