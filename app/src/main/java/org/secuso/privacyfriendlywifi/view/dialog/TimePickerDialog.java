package org.secuso.privacyfriendlywifi.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TimePicker;

import org.secuso.privacyfriendlywifi.logic.types.ScheduleEntry;
import org.secuso.privacyfriendlywifi.logic.util.IOnDialogClosedListener;

import java.util.ArrayList;
import java.util.Locale;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class TimePickerDialog implements IOnDialogClosedListener, DialogInterface.OnCancelListener {
    private final Context context;
    private int startHour = 22;
    private int startMinute = 0;
    private int endHour = 8;
    private int endMinute = 0;

    private ArrayList<IOnDialogClosedListener> onDialogClosedListeners;
    private int currentListSize = 0;

    private ViewPager viewPager;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private EditText titleEditText;
//    private TextView titleText;

    private AlertDialog alertDialog;

    public TimePickerDialog(Context context) {
        this.onDialogClosedListeners = new ArrayList<>();
        this.context = context;
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_timepicker_dialog, null, false);

        this.startTimePicker = (TimePicker) view.findViewById(R.id.start_time_picker);
        this.initPicker(startTimePicker, startHour, startMinute);

        this.endTimePicker = (TimePicker) view.findViewById(R.id.end_time_picker);
        this.initPicker(endTimePicker, endHour, endMinute);

        this.titleEditText = (EditText) view.findViewById(R.id.title_edit_text);
        this.titleEditText.setHint(String.format(Locale.getDefault(), this.context.getString(R.string.time_picker_dialog_text_title_hint), this.currentListSize));

//        this.titleText = (TextView) view.findViewById(R.id.dialog_title_text);

        this.viewPager = (ViewPager) view.findViewById(R.id.viewpager_dialog);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MyAdapter());
        final ViewPager.OnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                // TODO use this in marshmallow? -> does special alertdialog style work?
                //titleText.setText(viewPager.getAdapter().getPageTitle(position));
                setDialogTitle(position);

                if (position == 2) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(R.string.time_picker_dialog_button_finish);
                    titleEditText.requestFocus();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(titleEditText, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(R.string.time_picker_dialog_button_next);
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(titleEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        };
        viewPager.addOnPageChangeListener(listener);

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setPositiveButton(R.string.time_picker_dialog_button_next, null);
        builder.setNegativeButton(R.string.time_picker_dialog_button_cancel, null);
        builder.setTitle(R.string.time_picker_dialog_title_choose_start_time);
        builder.setView(view);

        this.alertDialog = builder.create();
        this.alertDialog.setCancelable(true);
        this.alertDialog.setCanceledOnTouchOutside(true);
        this.alertDialog.setOnCancelListener(this);
        this.alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                               @Override
                                               public void onShow(DialogInterface dialog) {

                                                   alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                                                           new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   if (viewPager.getCurrentItem() == 2) {
                                                                       onDialogClosed(DialogInterface.BUTTON_POSITIVE);
                                                                       alertDialog.dismiss();
                                                                   } else {
                                                                       viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                                                   }
                                                               }
                                                           });

                                                   alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                                                           new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   alertDialog.cancel();
                                                               }
                                                           });

                                                   listener.onPageSelected(0);
                                               }
                                           }

        );


        alertDialog.show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.alertDialog.dismiss();
        this.onDialogClosed(DialogInterface.BUTTON_NEGATIVE);
    }

    @Override
    public void onDialogClosed(int returnCode, Object... returnValue) {
        if (returnCode == DialogInterface.BUTTON_POSITIVE) {
            ScheduleEntry newEntry;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                newEntry = new ScheduleEntry(
                        (titleEditText.getText().toString().trim().isEmpty() ?
                                titleEditText.getHint().toString()
                                : titleEditText.getText().toString()),
                        startTimePicker.getHour(),
                        startTimePicker.getMinute(),
                        endTimePicker.getHour(),
                        endTimePicker.getMinute()
                );
            } else {
                newEntry = new ScheduleEntry(
                        (titleEditText.getText().toString().trim().isEmpty() ?
                                titleEditText.getHint().toString()
                                : titleEditText.getText().toString()),
                        startTimePicker.getCurrentHour(),
                        startTimePicker.getCurrentMinute(),
                        endTimePicker.getCurrentHour(),
                        endTimePicker.getCurrentMinute()
                );
            }

            for (IOnDialogClosedListener listener : onDialogClosedListeners) {
                listener.onDialogClosed(returnCode, newEntry);
            }
        } else {
            for (IOnDialogClosedListener listener : onDialogClosedListeners) {
                listener.onDialogClosed(returnCode, returnValue);
            }
        }
    }

    public boolean addOnDialogClosedListener(IOnDialogClosedListener listener) {
        return this.onDialogClosedListeners.add(listener);
    }

    public boolean removeOnDialogClosedListener(IOnDialogClosedListener listener) {
        return this.onDialogClosedListeners.remove(listener);
    }

    public void setCurrentListSize(int currentListSize) {
        this.currentListSize = currentListSize;
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

    private void setDialogTitle(int page) {
        alertDialog.setTitle(this.viewPager.getAdapter().getPageTitle(page));
    }

    private class MyAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return viewPager.getChildAt(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return context.getString(R.string.time_picker_dialog_title_choose_start_time);
                case 1:
                    return context.getString(R.string.time_picker_dialog_title_choose_end_time);
                case 2:
                    return context.getString(R.string.time_picker_dialog_title_choose_title);
            }
            return super.getPageTitle(position);
        }

        @Override
        public int getCount() {
            return viewPager.getChildCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            int pos = getItemPosition(object);
            viewPager.removeView(viewPager.getChildAt(pos));
        }
    }
}