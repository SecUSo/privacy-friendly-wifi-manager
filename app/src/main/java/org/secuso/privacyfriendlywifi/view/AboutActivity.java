package org.secuso.privacyfriendlywifi.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import secuso.org.privacyfriendlywifi.BuildConfig;
import secuso.org.privacyfriendlywifi.R;

/**
 * About page.
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView versionTextView = (TextView)
                findViewById(R.id.about_version);
        String versionText = getString(R.string.about_version);
        versionTextView.setText(String.format(versionText, BuildConfig.VERSION_NAME));
    }
}
