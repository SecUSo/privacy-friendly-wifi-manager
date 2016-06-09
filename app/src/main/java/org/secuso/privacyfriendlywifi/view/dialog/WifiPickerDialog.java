package org.secuso.privacyfriendlywifi.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.logic.util.OnDialogClosedListener;

import java.util.ArrayList;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class WifiPickerDialog implements OnDialogClosedListener, DialogInterface.OnCancelListener {
    private final Context context;
    private ArrayList<OnDialogClosedListener> onDialogClosedListeners;
    //    private TextView titleText;
    private AlertDialog alertDialog;

    public WifiPickerDialog(Context context) {
        this.onDialogClosedListeners = new ArrayList<>();
        this.context = context;
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_timepicker_dialog, null, false);

//        this.titleText = (TextView) view.findViewById(R.id.dialog_title_text);

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setPositiveButton(R.string.time_picker_dialog_button_finish, null);
        builder.setNegativeButton(R.string.time_picker_dialog_button_cancel, null);
        builder.setTitle("Select Wifi");
        builder.setView(view);

        this.alertDialog = builder.create();
        this.alertDialog.setCancelable(true);
        this.alertDialog.setCanceledOnTouchOutside(true);
        this.alertDialog.setOnCancelListener(this);
        this.alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                               @Override
                                               public void onShow(DialogInterface dialog) {
                                                   alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                                                           new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   alertDialog.cancel();
                                                               }
                                                           });
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
            WifiLocationEntry newEntry = new WifiLocationEntry("","");

            for (OnDialogClosedListener listener : onDialogClosedListeners) {
                listener.onDialogClosed(returnCode, newEntry);
            }
        } else {
            for (OnDialogClosedListener listener : onDialogClosedListeners) {
                listener.onDialogClosed(returnCode, returnValue);
            }
        }
    }

    public boolean addOnDialogClosedListener(OnDialogClosedListener listener) {
        return this.onDialogClosedListeners.add(listener);
    }

    public boolean removeOnDialogClosedListener(OnDialogClosedListener listener) {
        return this.onDialogClosedListeners.remove(listener);
    }
}