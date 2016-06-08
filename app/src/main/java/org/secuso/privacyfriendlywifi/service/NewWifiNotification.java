package org.secuso.privacyfriendlywifi.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import org.secuso.privacyfriendlywifi.view.MainActivity;

import secuso.org.privacyfriendlywifi.R;

/**
 * This class provides functionality to show a notification for newly connected wireless networks.
 * The user is able to add the current wifi to the managed connections using the action button.
 */
public class NewWifiNotification {

    public static void show(Context context, String wifiSsid) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // use InboxStyle to overcome layout issues
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        // sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle(context.getString(R.string.notification_unknown_wifi_title));
        inboxStyle.addLine(wifiSsid);

        // intent for action button click
        Intent buttonIntent = new Intent(context, NotificationButtonListener.class);
        PendingIntent pendingButtonIntent = PendingIntent.getBroadcast(context, 0,
                buttonIntent, 0);

        // intent for main notification area click
        Intent clickIntent = new Intent(context, NotificationClickListener.class);
        PendingIntent pendingClickIntent = PendingIntent.getBroadcast(context, 0,
                clickIntent, 0);

        // load large icon
        Bitmap large_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_wifi);

        // now assemble the notification
        Notification notification = new NotificationCompat.Builder(context)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setStyle(inboxStyle)
                .setSmallIcon(R.drawable.ic_action_wifi)
                .setLargeIcon(large_icon)
                .setContentTitle(context.getString(R.string.notification_unknown_wifi_title_format, wifiSsid))
                .setContentText(context.getString(R.string.notification_main_text))
                .setTicker(context.getString(R.string.notification_unknown_wifi_title_format, wifiSsid))
                .setContentInfo(context.getString(R.string.notification_content_info))
                .setWhen(0) // first element in notification stack - needed to get expanded notification
                .setPriority(NotificationCompat.PRIORITY_MAX) // enhance priority to get an expanded notification
                .addAction(R.drawable.ic_action_wifi, context.getString(R.string.notification_button_add), pendingButtonIntent)
                .setContentIntent(pendingClickIntent)
                .build();

        // show notification - always use notification id 1 since we only have one connected wifi
        notificationManager.notify(1, notification);
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
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // clear notification since auto dismiss does not work with action buttons
            notificationManager.cancel(1);

            // start activity
            Intent notificationIntent = new Intent(context, MainActivity.class); // TODO add flag for "add Wi-Fi"
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(notificationIntent);


        }
    }
}
