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

package org.secuso.privacyfriendlywifimanager.view.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.secuso.privacyfriendlywifimanager.logic.preconditions.ScheduleCondition;
import org.secuso.privacyfriendlywifimanager.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.IOnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifimanager.view.adapter.RemovableRecyclerViewAdapter;

import java.util.Locale;

import secuso.org.privacyfriendlywifi.R;

/**
 * ItemViewHolder for ScheduleEntry.
 */
public class ScheduleItemViewHolder extends RemovableItemViewHolder<ScheduleEntry> {
    private TextView titleTextView;
    private TextView timeTextView;
    private Switch statusSwitch;

    public ScheduleItemViewHolder(View itemView) {
        super(itemView);

        this.titleTextView = (TextView) itemView.findViewById(R.id.title);
        this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
        this.statusSwitch = (Switch) itemView.findViewById(R.id.status_switch);
    }

    public void setupItem(Context context, ScheduleEntry entry, RemovableRecyclerViewAdapter<ScheduleEntry> adapter, IOnDeleteModeChangedListener listener) {
        super.setupItem(context, entry, adapter, listener);

        String timeText = context.getString(R.string.schedule_time_text);

        ScheduleCondition scheduleCondition = entry.getScheduleCondition();

        this.titleTextView.setText(entry.getTitle());
        this.timeTextView.setText(String.format(Locale.getDefault(), timeText,
                scheduleCondition.getStartHour(), scheduleCondition.getStartMinute(),
                scheduleCondition.getEndHour(), scheduleCondition.getEndMinute()));

        this.statusSwitch.setChecked(scheduleCondition.isActive());
        this.statusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewEntry.getScheduleCondition().setActive(statusSwitch.isChecked());
            }
        });
    }
}
