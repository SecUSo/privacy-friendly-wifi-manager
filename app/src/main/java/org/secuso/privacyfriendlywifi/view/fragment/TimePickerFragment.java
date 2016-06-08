package org.secuso.privacyfriendlywifi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

/**
 *
 */
public class TimePickerFragment extends Fragment {
    private int position;
    private LinearLayout contentView;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private EditText titleEditText;

    public void setValues(int position, TimePicker startTimePicker, TimePicker endTimePicker, EditText titleEditText) {
        this.position = position;
        this.startTimePicker = startTimePicker;
        this.endTimePicker = endTimePicker;
        this.titleEditText = titleEditText;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.contentView = new LinearLayout(inflater.getContext());
        this.contentView.setOrientation(LinearLayout.VERTICAL);

        if (position == 0) {
            if (this.startTimePicker == null) {
                this.startTimePicker = new TimePicker(inflater.getContext());
            }
            this.contentView.addView(this.startTimePicker);
        } else if (position == 1) {
            if (this.endTimePicker == null) {
                this.endTimePicker = new TimePicker(inflater.getContext());
            }
            this.contentView.addView(this.endTimePicker);
        } else if (position == 2) {
            if (this.titleEditText == null) {
                this.titleEditText = new EditText(inflater.getContext());
            }
            this.contentView.addView(titleEditText);
            this.contentView.setPadding(this.contentView.getPaddingLeft(), 20, this.contentView.getPaddingRight(), 20);
        }

        return contentView;
    }

    @Override
    public void onDestroyView() {
        this.contentView.removeAllViewsInLayout();
        super.onDestroyView();
    }
}