/*
 This file is part of Privacy Friendly Wifi Manager.
 Privacy Friendly Wifi Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Wifi Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Wifi Manager. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlywifi.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import org.secuso.privacyfriendlywifi.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;
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
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews widgetActiveButtonView = updateButton(context, SettingsEntry.isServiceActive(context), false);

        // intent for widget button click
        Intent buttonIntent = new Intent(context, WidgetClickListener.class);
        PendingIntent pendingButtonIntent = PendingIntent.getBroadcast(context, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        widgetActiveButtonView.setOnClickPendingIntent(R.id.appwidget_switch, pendingButtonIntent);
        widgetActiveButtonView.setOnClickPendingIntent(R.id.appwidget_text, pendingButtonIntent);

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
        return updateButton(context, active, true);
    }

    /**
     * Updates the on/off button in every widget instance
     *
     * @param context   A Context.
     * @param active    Whether the button should be on (true) or off (false).
     * @param broadcast Whether this update should trigger a broadcast or not.
     * @return RemoveViews referring to the button instances.
     */
    private static RemoteViews updateButton(Context context, boolean active, boolean broadcast) {
        RemoteViews buttonView = new RemoteViews(context.getPackageName(), R.layout.widget_wifi);
        buttonView.setTextViewText(R.id.appwidget_switch, active ? context.getString(R.string.on) : context.getString(R.string.off));
        buttonView.setInt(R.id.appwidget_switch, "setBackgroundColor", active ? ContextCompat.getColor(context, R.color.colorAccent) : ContextCompat.getColor(context, R.color.colorPrimary));

        // now update the widgets
        if (broadcast) {
            broadcastUpdate(context);
        }

        return buttonView;
    }

    /**
     * Broadcasts that widget needs to be updated.
     *
     * @param context A context to use.
     */
    private static void broadcastUpdate(Context context) {
        final Intent updateWidgetsIntent = new Intent(context, WifiWidget.class);
        updateWidgetsIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WifiWidget.class));
        updateWidgetsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(updateWidgetsIntent);
    }

    /**
     * Listener for click event in main notification area. Starts MainActivity.
     */
    public static class WidgetClickListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean active = !SettingsEntry.isServiceActive(context);
            SettingsEntry.setActiveFlag(context, active);

            if (active) {
                Controller.registerReceivers(context);
            } else {
                Controller.unregisterReceivers(context);
            }

            updateButton(context, active);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        StaticContext.setContext(context);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

