package dk.au.mad21fall.appproject.group3.Other;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import dk.au.mad21fall.appproject.group3.Activities.MainActivity;
import dk.au.mad21fall.appproject.group3.R;

public class Notification_receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notification_intent = new Intent(context, MainActivity.class);
        notification_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 201, notification_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelID = context.getString(R.string.notificationChannelID);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setContentIntent(pendingIntent)
                .setContentTitle(context.getString(R.string.notificationTitle))
                .setContentText(context.getString(R.string.notificationText))
                .setSmallIcon(R.drawable.beer_vector)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        int weekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);


        if (weekDay == 6) {
            notificationManager.notify(201, builder.build());
        }

        Log.d("notification", "A new notification was posted");

    }
}
