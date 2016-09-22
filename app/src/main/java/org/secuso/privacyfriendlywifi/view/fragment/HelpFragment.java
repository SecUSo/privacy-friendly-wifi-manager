package org.secuso.privacyfriendlywifi.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.secuso.privacyfriendlywifi.view.TutorialActivity;

import secuso.org.privacyfriendlywifi.R;

;

/**
 * The help fragment. Note that this does not have a related xml file.
 */
public class HelpFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        final HelpFragment self = this;

        // Set substring in actionbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setSubtitle(R.string.help_actionbar_string);
        }

        // load entries for help page
        addPreferencesFromResource(R.xml.help_elements);

        // handler for tutorial
        Preference launchTutorialPref = findPreference("launchTutorial");
        launchTutorialPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent startAboutIntent = new Intent(getContext(), TutorialActivity.class);
                startAboutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                self.startActivity(startAboutIntent);
                return true;
            }
        });
    }
}
