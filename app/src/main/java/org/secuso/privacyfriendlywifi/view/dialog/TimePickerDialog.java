package org.secuso.privacyfriendlywifi.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;

import org.secuso.privacyfriendlywifi.logic.util.OnDialogClosedListener;

/**
 *
 */
public class TimePickerDialog extends AlertDialog {
    private Context context;
    private OnDialogClosedListener onDialogClosedListener;
    private String title;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    private EditText titleEditText;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private AlertDialog dialog;

    private int currentPage;
    private Builder builder;

    public TimePickerDialog(Context context, OnDialogClosedListener onDialogClosedListener) {
        super(context);
        this.context = context;
        this.onDialogClosedListener = onDialogClosedListener;
        this.title = "";
        this.startHour = 22;
        this.startMinute = 0;
        this.endHour = 8;
        this.endMinute = 0;

        this.currentPage = 0;

        this.titleEditText = new EditText(context);
        this.titleEditText.setText(title);

        this.startTimePicker = new TimePicker(context);
        this.initPicker(startTimePicker, startHour, startMinute);

        this.endTimePicker = new TimePicker(context);
        this.initPicker(endTimePicker, endHour, endMinute);
    }

    @Override
    public void show() {
        this.builder = new Builder(context);

        // Add the buttons
        this.builder.setPositiveButton("Weiter", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // store new dates and type in preferences
                if (currentPage == 2) {
                    onDialogClosedListener.onDialogClosed(DialogInterface.BUTTON_POSITIVE, title, startHour, startMinute, endHour, endMinute);
                } else {
                    showDialog(++currentPage);
                }
            }
        });
        this.builder.setNegativeButton("Abbrechen", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onDialogClosedListener.onDialogClosed(DialogInterface.BUTTON_NEGATIVE);
            }
        });

        this.showDialog(0);
    }

    private void showDialog(int page) {
        if (page == 0) {
            this.builder.setTitle("Choose start time");
            this.builder.setView(startTimePicker);
        } else if (page == 1) {
            this.builder.setTitle("Choose end time");
            this.builder.setView(endTimePicker);
        } else if (page == 2) {
            this.builder.setTitle("Choose title");
            this.builder.setView(titleEditText);
        } else {
            return; // wrong use
        }

        // Create the AlertDialog
        this.dialog = this.builder.create();
        if (this.dialog.isShowing()) {
            Log.e("TAG", "DIALOG IS SHOWING");
        }

        this.dialog.show();
    }

    private void initPicker(TimePicker picker, int hour, int minute) {
        picker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.setHour(hour);
            picker.setMinute(minute);
        } else {
            picker.setCurrentHour(hour);
            picker.setCurrentMinute(minute);
        }
    }
}