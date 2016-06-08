package org.secuso.privacyfriendlywifi.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import org.secuso.privacyfriendlywifi.service.Controller;

import secuso.org.privacyfriendlywifi.R;


/**
 * A widget that is able to start/stop the ManagerService via the Controller.
 */
public class WifiWidget extends AppWidgetProvider {

    /**
     * Updates an app widget instance
     *
     * @param context          A Context.
     * @param appWidgetManager An AppWidgetManager instance.
     * @param appWidgetId      The ID of the widget to update.
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences preferences = context.getSharedPreferences(MainActivity.PREF_SETTINGS, Context.MODE_PRIVATE);
        boolean active = preferences.getBoolean(MainActivity.PREF_ENTRY_SERVICE_ACTIVE, false);

        RemoteViews widgetActiveButtonView = updateButton(context, active);

        // intent for widget button click
        Intent buttonIntent = new Intent(context, WidgetClickListener.class);

        PendingIntent pendingButtonIntent = PendingIntent.getBroadcast(context, 0,
                buttonIntent, 0);

        widgetActiveButtonView.setOnClickPendingIntent(R.id.appwidget_switch, pendingButtonIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widgetActiveButtonView);
    }

    /**
     * Updates the on/off button in every widget instance
     *
     * @param context A Context.
     * @param active  Whether the button should be on (true) or off (false).
     * @return RemoveViews referring to the button instances.
     */
    private static RemoteViews updateButton(Context context, boolean active) {
        RemoteViews buttonView = new RemoteViews(context.getPackageName(), R.layout.wifi_widget);
        buttonView.setTextViewText(R.id.appwidget_switch, context.getString(R.string.appwidget_text, active ? context.getString(R.string.on) : context.getString(R.string.off)));

        final Intent updateWidgetsIntent = new Intent(context, WifiWidget.class);
        updateWidgetsIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context.getApplicationContext()).getAppWidgetIds(new ComponentName(context.getApplicationContext(), WifiWidget.class));
        updateWidgetsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

        // now update the widgets
        context.sendBroadcast(updateWidgetsIntent);

        return buttonView;
    }

    /**
     * Listener for click event in main notification area. Starts MainActivity.
     */
    public static class WidgetClickListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences preferences = context.getSharedPreferences(MainActivity.PREF_SETTINGS, Context.MODE_PRIVATE);
            boolean active = !preferences.getBoolean(MainActivity.PREF_ENTRY_SERVICE_ACTIVE, false);
            preferences.edit().putBoolean(MainActivity.PREF_ENTRY_SERVICE_ACTIVE, active).apply();


            if (active) {
                Controller.registerReceivers(context.getApplicationContext());
            } else {
                Controller.unregisterReceivers(context.getApplicationContext());
            }

            updateButton(context, active);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

