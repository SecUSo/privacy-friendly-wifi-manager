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

import java.util.List;

import secuso.org.privacyfriendlywifi.R;

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
                borderRow.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                table.addView(borderRow);

                TableRow contentRow = new TableRow(this);

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
                StringBuilder sb = new StringBuilder();
                for (PrimitiveCellInfo cellInfo : condition.getRelatedCells()) {
                    sb.append(cellInfo.getCellId()).append("\n");
                }
                cellIdsText.setText(sb.toString());
                contentRow.addView(cellIdsText);

                table.addView(contentRow);
            }
        }
    }
}
