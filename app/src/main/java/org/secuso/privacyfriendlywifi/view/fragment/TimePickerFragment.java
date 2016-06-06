package org.secuso.privacyfriendlywifi.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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

    private ViewPager pager;
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


        this.titleEditText = new EditText(inflater.getContext());
        this.titleEditText.setHint(title);
        this.titleEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        this.titleEditText.setMaxLines(1);

        this.linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_timepicker_dialog, container);

        this.pager = (ViewPager) this.linearLayout.findViewById(R.id.viewpager_dialog);
        this.pager.setAdapter(new FragPageAdapter(getChildFragmentManager()));
        this.pager.setOffscreenPageLimit(2);
        ViewPager.OnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                adaptPagerLayoutHeight();
                setDialogTitle(position);

                if (position == 2) {
                    titleEditText.requestFocus();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(titleEditText, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(titleEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        };
        this.pager.addOnPageChangeListener(listener);
        this.pager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                adaptPagerLayoutHeight();
            }
        });

        this.nextButton = (Button) this.linearLayout.findViewById(R.id.button_next);
        this.nextButton.setText("Weiter");
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == 2) {
                    onDialogClosed(DialogInterface.BUTTON_POSITIVE, title, startHour, startMinute, endHour, endMinute);
                    getDialog().dismiss();
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }
            }
        });

        listener.onPageSelected(0);
        return this.linearLayout;
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

    private void adaptPagerLayoutHeight() {
        View view = pager.getChildAt(pager.getCurrentItem());
        if (view != null) {
            view.measure(0, 0);
            pager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, view.getMeasuredHeight()));
        }
    }

    private void setDialogTitle(int page) {
        if (page == 0) {
            getDialog().setTitle("Choose start time");
        } else if (page == 1) {
            getDialog().setTitle("Choose end time");
        } else if (page == 2) {
            getDialog().setTitle("Choose title");
        }
    }

    private class FragPageAdapter extends FragmentStatePagerAdapter {

        public FragPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MyFrag frag = new MyFrag();
            frag.setValues(position, startTimePicker, endTimePicker, titleEditText);
            return frag;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}