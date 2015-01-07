package com.gf.movie.reminder.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.GameTrailerActivity;
import com.gf.movie.reminder.activity.MovieTrailerActivity;
import com.gf.movie.reminder.data.model.MovieReminder;
import com.gf.movie.reminder.data.model.Reminder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent receivedIntent) {
        Reminder reminder = receivedIntent.getParcelableExtra(NotificationManager.EXTRA_MOVIE_REMINDER);

        Intent intent;
        if (reminder instanceof MovieReminder) {
            intent = new Intent(context, MovieTrailerActivity.class);
        } else {
            intent = new Intent(context, GameTrailerActivity.class);
        }
        intent.putExtras(receivedIntent.getExtras());
        intent.putExtra(MovieTrailerActivity.EXTRA_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setContentTitle(reminder.getTrailer().getTitleString())
                .setContentText(reminder.getTrailer().getDescription())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        new SendNotification(context, notificationBuilder).execute(reminder);

        MovieReminderManager.getInstance(context).deleteReminder(reminder);
    }

    private class SendNotification extends AsyncTask<Reminder, Void, Bitmap> {

        android.app.NotificationManager mNotificationManager;
        private Notification.Builder mNotificationBuilder;

        public SendNotification(Context context, Notification.Builder notificationBuilder) {
            mNotificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationBuilder = notificationBuilder;
        }

        @Override
        protected Bitmap doInBackground(Reminder... params) {
            try {
                URL url = new URL(params[0].getTrailer().getImageUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                return BitmapFactory.decodeStream(connection.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            try {
                Notification notification = mNotificationBuilder.setLargeIcon(result).build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(0, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
