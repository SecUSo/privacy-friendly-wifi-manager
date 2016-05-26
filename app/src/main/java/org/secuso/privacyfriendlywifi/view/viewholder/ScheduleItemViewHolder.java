package org.secuso.privacyfriendlywifi.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.preconditions.ScheduleCondition;
import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;

import java.util.Locale;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class ScheduleItemViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private TextView timeTextView;
    private Switch statusSwitch;

    public ScheduleItemViewHolder(View itemView) {
        super(itemView);
        this.titleTextView = (TextView) itemView.findViewById(R.id.title);
        this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
        this.statusSwitch = (Switch) itemView.findViewById(R.id.status_switch);
        // onClickListener should be here
    }

    public void setupItem(Context context, ScheduleEntry entry) {
        String timeText = context.getString(R.string.schedule_time_text);
        ScheduleCondition condition = entry.getScheduleCondition();

        this.titleTextView.setText(entry.getTitle());
        this.timeTextView.setText(String.format(Locale.getDefault(), timeText, condition.getStartHour(), condition.getStartMinute(), condition.getEndHour(), condition.getEndMinute()));
        this.statusSwitch.setChecked(condition.isActive());
    }
}
