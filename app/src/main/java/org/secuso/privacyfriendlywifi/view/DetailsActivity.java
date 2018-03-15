/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifi.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.preconditions.CellLocationCondition;
import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfo;
import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.ScreenHandler;

import java.util.List;

import secuso.org.privacyfriendlywifi.R;

/**
 * Used to show details of a WiFi (cells and MACs).
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView ssidText = (TextView) findViewById(R.id.wifi_ssid);
        TableLayout table = (TableLayout) findViewById(R.id.details_table);

        WifiLocationEntry entry = (WifiLocationEntry) getIntent().getSerializableExtra(WifiLocationEntry.class.getSimpleName());

        if (ssidText != null && entry != null && table != null) {
            ssidText.setText("".equals(entry.getSsid().trim()) ? getString(R.string.wifi_hidden_wifi_text) : entry.getSsid());

            // insert content rows
            List<CellLocationCondition> cellLocationConditions = entry.getCellLocationConditions();

            for (CellLocationCondition condition : cellLocationConditions) {
                // add row border
                TableRow borderRow = new TableRow(this);
                borderRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                borderRow.setMinimumHeight(1);
                borderRow.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                table.addView(borderRow);

                TableRow contentRow = new TableRow(this);
                contentRow.setPadding(contentRow.getPaddingLeft(), ScreenHandler.getPXFromDP(4, this), contentRow.getPaddingRight(), ScreenHandler.getPXFromDP(4, this));

                // create MAC text cell
                TextView macTextView = new TextView(this);
                macTextView.setGravity(Gravity.START | Gravity.LEFT);
                if (Build.VERSION.SDK_INT < 23) {
                    macTextView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                } else {
                    macTextView.setTextAppearance(android.R.style.TextAppearance_Small);
                }
                macTextView.setText(condition.getBssid());
                contentRow.addView(macTextView);

                // create cell IDs text cell
                TextView cellIdsText = new TextView(this);
                cellIdsText.setGravity(Gravity.END | Gravity.RIGHT);
                if (Build.VERSION.SDK_INT < 23) {
                    cellIdsText.setTextAppearance(this, android.R.style.TextAppearance_Small);
                } else {
                    cellIdsText.setTextAppearance(android.R.style.TextAppearance_Small);
                }
                if (!condition.getRelatedCells().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (PrimitiveCellInfo cellInfo : condition.getRelatedCells()) {
                        sb.append(cellInfo.getCellId()).append("\n");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                    cellIdsText.setText(sb.toString());
                } else {
                    cellIdsText.setText("-");
                }
                contentRow.addView(cellIdsText);

                table.addView(contentRow);
            }
        }
    }
}
