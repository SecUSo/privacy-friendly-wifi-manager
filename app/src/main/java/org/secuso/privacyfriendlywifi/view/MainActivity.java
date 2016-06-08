package org.secuso.privacyfriendlywifi.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;

import org.secuso.privacyfriendlywifi.logic.preconditions.CellLocationCondition;
import org.secuso.privacyfriendlywifi.service.Controller;
import org.secuso.privacyfriendlywifi.view.fragment.AboutFragment;
import org.secuso.privacyfriendlywifi.view.fragment.ScheduleFragment;
import org.secuso.privacyfriendlywifi.view.fragment.SettingsFragment;
import org.secuso.privacyfriendlywifi.view.fragment.WifiListFragment;

import secuso.org.privacyfriendlywifi.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static int DYN_PERMISSION = 0;
    private final static String PREF_SETTINGS = "SHARED_PREF_SETTINGS";
    private final static String PREF_ENTRY_SERVICE_ACTIVE = "SHARED_PREF_ENTRY_SERVICE_ACTIVE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setup the drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.d(TAG, "SHOW SOME EXPLANATION FOR LOCATION"); // TODO show some explanatory dialog
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION},
                DYN_PERMISSION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.main, menu);

        final SharedPreferences settings = getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE);


        // get action view
        final MenuItem toggleservice = menu.findItem(R.id.main_switch);
        final RelativeLayout switchOuter = (RelativeLayout) toggleservice.getActionView();
        final Switch mainSwitch = (Switch) switchOuter.findViewById(R.id.switchMain);
        mainSwitch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mainSwitch.isChecked()) {
                            Log.i(TAG, "Register all receivers.");

                            settings.edit().putBoolean(PREF_ENTRY_SERVICE_ACTIVE, true).apply();
                            Controller.registerReceivers(getApplicationContext());
                        } else {
                            Log.i(TAG, "Unregister all receivers.");
                            settings.edit().putBoolean(PREF_ENTRY_SERVICE_ACTIVE, false).apply();
                            Controller.unregisterReceivers(getApplicationContext());
                        }

                    }
                }
        );

        // update state switch´s state
        mainSwitch.setChecked(settings.getBoolean(PREF_ENTRY_SERVICE_ACTIVE, false));

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case DYN_PERMISSION: {
                if (grantResults.length >= 3) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        // we got wifi permissions

                        if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                            // we got coarse location permission
                            // switch to list of allowed wifis
                            this.switchToFragment(WifiListFragment.class, true);
                        } else {
                            // switch to an explaining dialog
                            this.switchToFragment(AboutFragment.class, true);
                        }
                    }


                }

                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer != null) {
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

        Class<? extends Fragment> fragmentClass = null;
        switch (item.getItemId()) {
            case R.id.nav_whitelist:
                if (CellLocationCondition.hasCoarseLocationPermission(this)) {
                    fragmentClass = WifiListFragment.class;
                } else {
                    fragmentClass = AboutFragment.class;
                }
                break;
            case R.id.nav_schedule:
                fragmentClass = ScheduleFragment.class;
                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_help:
                break;
            case R.id.nav_about:
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = WifiListFragment.class;
        }

        this.switchToFragment(fragmentClass);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer != null) {
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
            Log.e(TAG, "InstantiationException");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException");
            e.printStackTrace();
        }
    }
}
