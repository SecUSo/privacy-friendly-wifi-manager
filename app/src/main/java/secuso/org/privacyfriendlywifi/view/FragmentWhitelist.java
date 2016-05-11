package secuso.org.privacyfriendlywifi.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import secuso.org.privacyfriendlywifi.R;

public class FragmentWhitelist extends Fragment {

    public FragmentWhitelist() {
        // Required empty public constructor
    }

    public static FragmentWhitelist newInstance() {
        FragmentWhitelist fragment = new FragmentWhitelist();
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
        return inflater.inflate(R.layout.fragment_whitelist, container, false);
    }
}