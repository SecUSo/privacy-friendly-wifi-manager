package org.secuso.privacyfriendlywifi.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import org.secuso.privacyfriendlywifi.logic.util.AbstractSettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
import org.secuso.privacyfriendlywifi.service.Controller;
import org.secuso.privacyfriendlywifi.view.fragment.AboutFragment;
import org.secuso.privacyfriendlywifi.view.fragment.ScheduleFragment;
import org.secuso.privacyfriendlywifi.view.fragment.SettingsFragment;
import org.secuso.privacyfriendlywifi.view.fragment.WifiListFragment;

import secuso.org.privacyfriendlywifi.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    public final static int DYN_PERMISSION = 0;
    private Menu menu;

    private boolean isDrawerLocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticContext.setContext(this);

        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(SettingsEntry.PREF_SETTINGS, Context.MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean(SettingsEntry.PREF_ENTRY_FIRST_RUN, true);

        // show help activity on first run
        if (firstRun) {
            Logger.d(TAG, "First run.");
            Intent startHelpIntent = new Intent(this, HelpActivity.class);
            startHelpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.startActivity(startHelpIntent);
            this.overridePendingTransition(0, 0); // deactivate the transition between empty activity and help activity
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, DYN_PERMISSION);
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // setup the drawer layout
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            // set version string in navigation header
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            if (drawer != null && navigationView != null) {
                navigationView.setNavigationItemSelectedListener(this);

                if (getResources().getBoolean(R.bool.isTablet)) {
                    // we are on a tablet
                    Logger.e(TAG, "TABLET");
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                    drawer.setScrimColor(Color.TRANSPARENT);
                    this.isDrawerLocked = true;
                } else {
                    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                            R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                            super.onDrawerSlide(drawerView, 0); // this disables the animation
                        }
                    };

                    drawer.addDrawerListener(toggle);

                    toggle.syncState();
                }
            }

            if (AbstractSettingsEntry.hasCoarseLocationPermission(this)) {
                this.switchToFragment(WifiListFragment.class, true);
            } else {
                this.switchToFragment(ScheduleFragment.class, true);
            }
        }

        updateNavigationDrawerSelection(R.id.nav_whitelist);
    }

    private void updateNavigationDrawerSelection(int id) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            Menu nav_menu = navigationView.getMenu();

            nav_menu.findItem(id).setChecked(true);
            navigationView.postInvalidate();
        }
    }

    private void updateNavigationDrawerVisibility() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            Menu nav_menu = navigationView.getMenu();
            boolean visible = SettingsEntry.hasCoarseLocationPermission(this);

            nav_menu.findItem(R.id.nav_whitelist).setVisible(visible);
            navigationView.invalidate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu = menu;

        // setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        // get action view
        final MenuItem toggleservice = menu.findItem(R.id.main_switch);
        final RelativeLayout switchOuter = (RelativeLayout) toggleservice.getActionView();
        final Switch mainSwitch = (Switch) switchOuter.findViewById(R.id.switchMain);

        // intent to update all widgets
        final Intent updateWidgetsIntent = new Intent(this, WifiWidget.class);
        updateWidgetsIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WifiWidget.class));
        updateWidgetsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

        final MainActivity self = this;

        mainSwitch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mainSwitch.isChecked()) {
                            SettingsEntry.setActiveFlag(self, true);
                            Controller.registerReceivers(self);
                        } else {
                            SettingsEntry.setActiveFlag(self, false);
                            Controller.unregisterReceivers(self);
                        }

                        // now update the widgets
                        sendBroadcast(updateWidgetsIntent);
                    }
                }
        );

        // update state switch´s state
        mainSwitch.setChecked(SettingsEntry.isServiceActive(this));

        // set marginStart using measurement since drawer is locked
        if (this.isDrawerLocked) {

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            FrameLayout fragmentContent = (FrameLayout) findViewById(R.id.fragmentContent);
            if (fragmentContent != null && navigationView != null) {
                int width = navigationView.getMeasuredWidth();

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) fragmentContent.getLayoutParams();

                layoutParams.setMargins(width + layoutParams.leftMargin, (toolbar != null ? toolbar.getHeight() : 0) + layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);

                fragmentContent.setLayoutParams(layoutParams);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case DYN_PERMISSION: {
                if (grantResults.length >= 3) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        // we got wifi permissions

                        if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                            // we got coarse location permission
                            invalidateOptionsMenu();
                            // switch to list of allowed wifis
                            this.switchToFragment(WifiListFragment.class, true);
                        } else {
                            this.switchToFragment(ScheduleFragment.class, true);
                        }
                    }

                    // updateNavigationDrawerVisibility();
                }

                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // handle resume to ensure that a state change caused by the widget leads to updated UI
        if (this.menu != null) {

            // get action view
            MenuItem toggleservice = this.menu.findItem(R.id.main_switch);
            if (toggleservice != null) {
                RelativeLayout switchOuter = (RelativeLayout) toggleservice.getActionView();
                Switch mainSwitch = (Switch) switchOuter.findViewById(R.id.switchMain);

                // update state switch´s state
                mainSwitch.setChecked(SettingsEntry.isServiceActive(this));
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer != null && !isDrawerLocked) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();
        final MainActivity self = this;
        Handler h = new Handler();

        h.post(new Runnable() {
            @Override
            public void run() {
                Class<? extends Fragment> fragmentClass = null;
                updateNavigationDrawerSelection(id); // update selection in drawer

                switch (id) {
                    case R.id.nav_whitelist:
                        fragmentClass = WifiListFragment.class;
                        break;
                    case R.id.nav_schedule:
                        fragmentClass = ScheduleFragment.class;
                        break;
                    case R.id.nav_settings:
                        fragmentClass = SettingsFragment.class;
                        break;
                    case R.id.nav_help:
                        Intent startHelpIntent = new Intent(self, HelpActivity.class);
                        startHelpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        self.startActivity(startHelpIntent);
                        break;
                    case R.id.nav_about:
                        fragmentClass = AboutFragment.class;
                        break;
                    default:
                        fragmentClass = SettingsEntry.hasCoarseLocationPermission(self) ? WifiListFragment.class : ScheduleFragment.class;
                }

                self.switchToFragment(fragmentClass);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer != null && !this.isDrawerLocked) {
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void switchToFragment(Class<? extends Fragment> fragmentClass) {
        this.switchToFragment(fragmentClass, false);
    }

    private void switchToFragment(Class<? extends Fragment> fragmentClass, boolean force) {
        try {
            if (fragmentClass != null) {
                // Insert the fragment by replacing any existing fragment
                Fragment fragment = fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                @SuppressLint("CommitTransaction") FragmentTransaction trans = fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment);

                if (force) {
                    trans.commitAllowingStateLoss();
                } else {
                    trans.commit();
                }
            }
        } catch (InstantiationException e) {
            Logger.e(TAG, "InstantiationException");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Logger.e(TAG, "IllegalAccessException");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, "IllegalArgumentException");
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.flush();
    }
}
