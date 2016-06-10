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
public class ScheduleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private TextView titleTextView;
    private TextView timeTextView;
    private Switch statusSwitch;
    private ScheduleCondition scheduleCondition;

    public ScheduleItemViewHolder(View itemView) {
        super(itemView);
        this.titleTextView = (TextView) itemView.findViewById(R.id.title);
        this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
        this.statusSwitch = (Switch) itemView.findViewById(R.id.status_switch);
    }

    public void setupItem(Context context, ScheduleEntry entry) {
        String timeText = context.getString(R.string.schedule_time_text);

        this.scheduleCondition = entry.getScheduleCondition();

        this.titleTextView.setText(entry.getTitle());
        this.timeTextView.setText(String.format(Locale.getDefault(), timeText,
                this.scheduleCondition.getStartHour(), this.scheduleCondition.getStartMinute(),
                this.scheduleCondition.getEndHour(), this.scheduleCondition.getEndMinute()));

        this.statusSwitch.setChecked(this.scheduleCondition.isActive());
        this.statusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleCondition.setActive(statusSwitch.isChecked());
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
