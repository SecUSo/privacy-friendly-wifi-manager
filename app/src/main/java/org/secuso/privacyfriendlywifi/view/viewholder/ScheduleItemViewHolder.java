package org.secuso.privacyfriendlywifi.view.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.preconditions.ScheduleCondition;
import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.util.IOnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifi.view.adapter.RemovableRecyclerViewAdapter;

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
