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
public class TutorialFragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial_step2, container, false);
    }

    public static TutorialFragment2 newInstance() {

        TutorialFragment2 f = new TutorialFragment2();
        Bundle b = new Bundle();
        f.setArguments(b);

        return f;
    }
}