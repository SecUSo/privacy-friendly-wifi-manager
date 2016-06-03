package org.secuso.privacyfriendlywifi.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import org.secuso.privacyfriendlywifi.logic.util.OnDialogClosedListener;

import secuso.org.privacyfriendlywifi.R;

public class TimePickerFragment extends DialogFragment implements OnDialogClosedListener {
    private Context context;
    private String title = "Testblabbla";
    private int startHour = 22;
    private int startMinute = 0;
    private int endHour = 8;
    private int endMinute = 0;
    private int currentPage = 0;

    private LinearLayout linearLayout;
    private LinearLayout contentView;
    private EditText titleEditText;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private Button nextButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.context = inflater.getContext();

        this.startTimePicker = new TimePicker(inflater.getContext());
        this.initPicker(startTimePicker, startHour, startMinute);

        this.endTimePicker = new TimePicker(inflater.getContext());
        this.initPicker(endTimePicker, endHour, endMinute);

        this.nextButton = new Button(inflater.getContext(), null, android.support.design.R.attr.borderlessButtonStyle);
        this.nextButton.setText("Weiter");
        this.nextButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        this.nextButton.setTextColor(getResources().getColor(R.color.white));
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // store new dates and type in preferences
                if (currentPage == 2) {
                    onDialogClosed(DialogInterface.BUTTON_POSITIVE, title, startHour, startMinute, endHour, endMinute);
                    getDialog().dismiss();
                } else {
                    showDialog(++currentPage);
                }
            }
        });

        this.titleEditText = new EditText(inflater.getContext());
        this.titleEditText.setHint(title);
        this.titleEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        this.titleEditText.setMaxLines(1);

        this.contentView = new LinearLayout(context);
        this.contentView.setOrientation(LinearLayout.VERTICAL);

        this.linearLayout = new LinearLayout(context);
        this.linearLayout.setOrientation(LinearLayout.VERTICAL);
        this.linearLayout.addView(contentView);
        this.linearLayout.addView(nextButton);

        return this.showDialog(0);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        onDialogClosed(DialogInterface.BUTTON_NEGATIVE);
    }

    @Override
    public void onDialogClosed(int returnCode, Object... returnValue) {
        Toast.makeText(getActivity().getApplicationContext(), "Code: " + returnCode, Toast.LENGTH_SHORT).show();
    }

    private View showDialog(int page) {
        this.contentView.removeAllViewsInLayout();

        if (page == 0) {
            this.getDialog().setTitle("Choose start time");
            this.contentView.addView(startTimePicker);
        } else if (page == 1) {
            this.getDialog().setTitle("Choose end time");
            this.contentView.addView(endTimePicker);
        } else if (page == 2) {
            this.getDialog().setTitle("Choose title");
            this.contentView.addView(titleEditText);
            this.titleEditText.requestFocus();

            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInput(titleEditText, InputMethodManager.SHOW_IMPLICIT);
        }

        return this.linearLayout;
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