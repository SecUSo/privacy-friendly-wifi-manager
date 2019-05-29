/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import org.secuso.privacyfriendlywifimanager.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.StaticContext;
import org.secuso.privacyfriendlywifimanager.logic.util.WifiHandler;
import org.secuso.privacyfriendlywifimanager.logic.util.WifiListHandler;
import org.secuso.privacyfriendlywifimanager.service.receivers.AlarmReceiver;
import org.secuso.privacyfriendlywifimanager.view.MainActivity;
import org.secuso.privacyfriendlywifimanager.view.fragment.WifiListFragment;

import java.util.Locale;

import secuso.org.privacyfriendlywifi.R;

/**
 * This class provides functionality to show a notification for newly connected wireless networks.
 * The user is able to add the current wifi to the managed connections using the action button.
 */
public class WifiNotification {

    private static final String CHANNEL_ID = "defaultChannel";

    public static void show(Context context, WifiInfo wifiInfo) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // use InboxStyle to overcome layout issues
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        // sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle(context.getString(R.string.notification_unknown_wifi_title));
        inboxStyle.addLine("".equals(WifiHandler.getCleanSSID(wifiInfo.getSSID()).trim()) ? context.getString(R.string.wifi_hidden_wifi_text) : WifiHandler.getCleanSSID(wifiInfo.getSSID()));

        // intent for action button click
        Intent buttonIntent = new Intent(context, NotificationButtonListener.class);
        buttonIntent.putExtra(WifiLocationEntry.class.getSimpleName(), new String[]{wifiInfo.getSSID(), wifiInfo.getBSSID()});
        // create new intent and UPDATE THE EXTRAS (important!)
        PendingIntent pendingButtonIntent = PendingIntent.getBroadcast(context, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // intent for main notification area click
        Intent clickIntent = new Intent(context, NotificationClickListener.class);
        PendingIntent pendingClickIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);

        // load large icon
        Bitmap large_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_wifi);

        // now assemble the notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setStyle(inboxStyle)
                .setSmallIcon(R.drawable.ic_action_wifi)
                .setLargeIcon(large_icon)
                .setContentTitle(context.getString(R.string.notification_unknown_wifi_title_format, WifiHandler.getCleanSSID((wifiInfo.getSSID()))))
                .setContentText(context.getString(R.string.notification_main_text))
                .setTicker(context.getString(R.string.notification_unknown_wifi_title_format, WifiHandler.getCleanSSID((wifiInfo.getSSID()))))
                .setContentInfo(context.getString(R.string.notification_content_info))
                .setWhen(0) // first element in notification stack - needed to get expanded notification
                .setPriority(NotificationCompat.PRIORITY_MAX) // enhance priority to get an expanded notification
                .addAction(R.drawable.ic_action_wifi, context.getString(R.string.notification_button_add), pendingButtonIntent)
                .setContentIntent(pendingClickIntent)
                .build();

        // show notification - always use notification id 1 since we only have one connected wifi
        notificationManager.notify(1, notification);
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = context.getString(R.string.channel_name);
            String descriptionText = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(descriptionText);

            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Listener for click event in main notification area. Starts MainActivity.
     */
    public static class NotificationClickListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // clear notification since auto dismiss does not work with action buttons
            notificationManager.cancel(1);

            // start activity
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(notificationIntent);
        }
    }

    /**
     * Listener for the action button in the expanded notification.
     * Dismisses notification and starts MainActivity to add current WiFi.
     */
    public static class NotificationButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            StaticContext.setContext(context);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // clear notification since auto dismiss does not work with action buttons
            notificationManager.cancel(1);

            // add the new wifi to the list
            WifiListHandler wifiListHandler = new WifiListHandler(context);
            String[] wifiInfo = intent.getStringArrayExtra(WifiLocationEntry.class.getSimpleName());
            wifiListHandler.add(new WifiLocationEntry(WifiHandler.getCleanSSID(wifiInfo[0]), wifiInfo[1]));

            Intent refreshList = new Intent(context, WifiListFragment.class);
            refreshList.setAction("REFRESH_LIST");
            context.getApplicationContext().sendBroadcast(refreshList);

            if (SettingsEntry.isServiceActive(context)) {
                AlarmReceiver.fireAndSchedule(context);
            }

            Toast.makeText(context, String.format(Locale.getDefault(), context.getString(R.string.toast_wifi_added), WifiHandler.getCleanSSID(wifiInfo[0])), Toast.LENGTH_LONG).show();
        }
    }
}
