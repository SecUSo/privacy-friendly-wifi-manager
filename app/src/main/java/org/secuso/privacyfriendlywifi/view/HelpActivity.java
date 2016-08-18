package org.secuso.privacyfriendlywifi.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.view.fragment.HelpFragment0;
import org.secuso.privacyfriendlywifi.view.fragment.HelpFragment1;
import org.secuso.privacyfriendlywifi.view.fragment.HelpFragment2;

import secuso.org.privacyfriendlywifi.R;

public class HelpActivity extends AppCompatActivity {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(getString(R.string.help_actionbar_string));
        actionBar.setSubtitle(getString(R.string.help_actionbar_substring_welcome));

        final Button nextbutton = (Button) findViewById(R.id.nextbutton);
        nextbutton.setText(getString(R.string.button_next));

        // Button Listener
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getChildCount() > mViewPager.getCurrentItem())
                {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
                }
                else{
                    mViewPager.setCurrentItem(0);
                }


            }
        });


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        if (radioGroup != null) {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radioButton0:
                            mViewPager.setCurrentItem(0, true);
                            break;
                        case R.id.radioButton1:
                            mViewPager.setCurrentItem(1, true);
                            break;
                        case R.id.radioButton2:
                            mViewPager.setCurrentItem(2, true);
                            break;
                        case R.id.radioButton3:
                            mViewPager.setCurrentItem(3, true);
                            break;
                    }
                }
            });
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (radioGroup != null) {
                    switch (position) {
                        case 0:
                            radioGroup.check(R.id.radioButton0);
                            if (actionBar != null) {
                                actionBar.setTitle(getString(R.string.help_actionbar_string));
                                actionBar.setSubtitle(getString(R.string.help_actionbar_substring_welcome));
                                nextbutton.setText(getString(R.string.button_start));
                            }
                            break;
                        case 1:
                            radioGroup.check(R.id.radioButton1);
                            if (actionBar != null) {
                                actionBar.setTitle(getString(R.string.help_actionbar_string_basics));
                                actionBar.setSubtitle(getString(R.string.help_actionbar_substring_step1));
                                nextbutton.setText(getString(R.string.button_next));
                            }
                            break;
                        case 2:
                            radioGroup.check(R.id.radioButton2);
                            if (actionBar != null) {
                                actionBar.setTitle(getString(R.string.help_actionbar_string_basics));
                                actionBar.setSubtitle(getString(R.string.help_actionbar_substring_step2));
                                nextbutton.setText(getString(R.string.button_next));
                            }
                            break;
                        case 3:
                            radioGroup.check(R.id.radioButton3);
                            if (actionBar != null) {
                                actionBar.setTitle(getString(R.string.help_actionbar_string_basics));
                                actionBar.setSubtitle(getString(R.string.help_actionbar_substring_step3));
                                nextbutton.setText(getString(R.string.button_finish));
                            }
                            break;
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

            switch(position) {
                case 0:
                    return HelpFragment0.newInstance("Help: Welcome");
                case 1:
                    return HelpFragment1.newInstance("Help: Step 1");
                case 2:
                    return HelpFragment2.newInstance("Help: Step 2");
                default:
                    return HelpFragment0.newInstance("Help: Step 3");
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }
}
