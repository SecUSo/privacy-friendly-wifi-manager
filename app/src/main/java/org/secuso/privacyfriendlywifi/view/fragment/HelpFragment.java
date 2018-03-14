/*
 This file is part of Privacy Friendly Wifi Manager.
 Privacy Friendly Wifi Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Wifi Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Wifi Manager. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlywifi.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.secuso.privacyfriendlywifi.view.TutorialActivity;

import secuso.org.privacyfriendlywifi.R;

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
        if (launchTutorialPref != null) {
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
}
