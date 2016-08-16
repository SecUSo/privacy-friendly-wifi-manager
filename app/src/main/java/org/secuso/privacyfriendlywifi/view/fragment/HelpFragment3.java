package org.secuso.privacyfriendlywifi.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import secuso.org.privacyfriendlywifi.R;

public class HelpFragment3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help_step3, container, false);
    }

    public static HelpFragment3 newInstance(String text) {

        HelpFragment3 f = new HelpFragment3();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}