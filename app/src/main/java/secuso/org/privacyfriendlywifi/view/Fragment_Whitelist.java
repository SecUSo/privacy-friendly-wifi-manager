package secuso.org.privacyfriendlywifi.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import secuso.org.privacyfriendlywifi.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Whitelist.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Whitelist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Whitelist extends Fragment {

    public Fragment_Whitelist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment_Whitelist.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Whitelist newInstance() {
        Fragment_Whitelist fragment = new Fragment_Whitelist();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        Log.e("FRAGMENT", "HELLO WORLD!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_whitelist, container, false);
    }
}
