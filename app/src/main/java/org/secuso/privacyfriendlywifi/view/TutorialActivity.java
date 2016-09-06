package org.secuso.privacyfriendlywifi.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.secuso.privacyfriendlywifi.logic.util.AbstractSettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.view.fragment.TutorialFragment0;
import org.secuso.privacyfriendlywifi.view.fragment.TutorialFragment1;
import org.secuso.privacyfriendlywifi.view.fragment.TutorialFragment2;
import org.secuso.privacyfriendlywifi.view.fragment.TutorialFragment3;
import org.secuso.privacyfriendlywifi.view.fragment.TutorialFragment4;

import java.util.ArrayList;
import java.util.List;

import secuso.org.privacyfriendlywifi.R;

public class TutorialActivity extends AppCompatActivity {
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ActionBar actionBar;
    private List<Fragment> fragments = new ArrayList<>();
    private List<RadioButton> radioButtons = new ArrayList<>();
    private final int radio_id_offset = 1337; // offset to make sure that there are no interfering ids

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        final SharedPreferences prefs = getSharedPreferences(SettingsEntry.PREF_SETTINGS, Context.MODE_PRIVATE);
        final boolean firstRun = prefs.getBoolean(SettingsEntry.PREF_ENTRY_FIRST_RUN, true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null && !firstRun) {
            // hide back button on first run
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(getString(R.string.help_category_tutorial));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        // Add necessary fragments
        fragments.add(TutorialFragment0.newInstance());

        if (!AbstractSettingsEntry.hasCoarseLocationPermission(this)) {
            fragments.add(TutorialFragment1.newInstance());
        }

        fragments.add(TutorialFragment2.newInstance());
        fragments.add(TutorialFragment3.newInstance());
        fragments.add(TutorialFragment4.newInstance());

        // set up radio buttons
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        if (radioGroup != null) {
            for (int i = 0; i < this.fragments.size(); i++) {
                RadioButton toAdd = new RadioButton(this);
                toAdd.setId(radio_id_offset + i);
                this.radioButtons.add(toAdd);
                radioGroup.addView(toAdd);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    mViewPager.setCurrentItem(checkedId - radio_id_offset, true);
                }
            });

            mViewPager.setCurrentItem(0, true);
            radioGroup.check(this.radioButtons.get(0).getId());
        }

        final Button nextbutton = (Button) findViewById(R.id.nextbutton);
        nextbutton.setText(getString(R.string.button_start));

        final AppCompatActivity thisActivity = this;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Button Listener
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSectionsPagerAdapter.getCount() > (mViewPager.getCurrentItem() + 1)) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                } else {
                    if (firstRun) {
                        // set help finished on first run and start main activity as new root
                        prefs.edit().putBoolean(SettingsEntry.PREF_ENTRY_FIRST_RUN, false).apply();
                        Intent startMainIntent = new Intent(thisActivity, MainActivity.class);
                        startMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(startMainIntent);
                    } else {
                        // return to main activity
                        finish();
                    }
                }


            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (radioGroup != null) {
                    int id = radioButtons.get(position).getId();
                    radioGroup.check(id);
                    if (position == 0) {
                        actionBar.setTitle(getString(R.string.help_actionbar_string));
                        nextbutton.setText(getString(R.string.button_start));
                    } else {
                        actionBar.setTitle(getString(R.string.help_actionbar_string_basics));

                        if (position < fragments.size() - 1) {
                            nextbutton.setText(getString(R.string.button_next));
                        } else {
                            nextbutton.setText(getString(R.string.button_finish));
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return (position < fragments.size() ? fragments.get(position) : fragments.get(0));
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
