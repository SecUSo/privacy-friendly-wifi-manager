package org.secuso.privacyfriendlywifi.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.preconditions.ScheduleCondition;
import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.util.OnDeleteModeChangedListener;
import org.secuso.privacyfriendlywifi.view.adapter.ScheduleAdapter;

import java.util.Locale;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class ScheduleItemViewHolder extends RecyclerView.ViewHolder implements /*View.OnClickListener,*/ View.OnLongClickListener {
    private ImageButton deleteButton;
    private TextView titleTextView;
    private TextView timeTextView;
    private Switch statusSwitch;

    private ScheduleEntry scheduleEntry;
    private ScheduleAdapter adapter;
    private OnDeleteModeChangedListener listener;

    public ScheduleItemViewHolder(View itemView) {
        super(itemView);
        this.deleteButton = (ImageButton) itemView.findViewById(R.id.button_delete);
        this.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(scheduleEntry);
            }
        });
        this.titleTextView = (TextView) itemView.findViewById(R.id.title);
        this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
        this.statusSwitch = (Switch) itemView.findViewById(R.id.status_switch);
        itemView.setOnLongClickListener(this);
    }

    public void setupItem(Context context, ScheduleEntry entry, ScheduleAdapter adapter, OnDeleteModeChangedListener listener) {
        String timeText = context.getString(R.string.schedule_time_text);

        this.scheduleEntry = entry;

        ScheduleCondition scheduleCondition = entry.getScheduleCondition();
        this.adapter = adapter;
        this.listener = listener;

        this.setDeleteButtonVisible(this.adapter.isDeleteModeActive());

        this.titleTextView.setText(entry.getTitle());
        this.timeTextView.setText(String.format(Locale.getDefault(), timeText,
                scheduleCondition.getStartHour(), scheduleCondition.getStartMinute(),
                scheduleCondition.getEndHour(), scheduleCondition.getEndMinute()));

        this.statusSwitch.setChecked(scheduleCondition.isActive());
        this.statusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleEntry.getScheduleCondition().setActive(statusSwitch.isChecked());
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        this.listener.setDeleteModeActive(true);
        return true;
    }

    public void setDeleteButtonVisible(boolean visible) {
        if (visible) {
            this.deleteButton.setVisibility(View.VISIBLE);
        } else {
            this.deleteButton.setVisibility(View.GONE);
        }
    }
}
