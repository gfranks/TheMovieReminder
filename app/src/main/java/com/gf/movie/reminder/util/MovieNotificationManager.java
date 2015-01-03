package com.gf.movie.reminder.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminder;

import java.util.Date;

public class MovieNotificationManager {

    public static final String EXTRA_MOVIE_REMINDER = "reminder";

    private static MovieNotificationManager mInstance;

    private AccountManager mAccountManager;
    private MovieReminderManager mReminderManager;

    private MovieNotificationManager(AccountManager accountManager, MovieReminderManager reminderManager) {
        mAccountManager = accountManager;
        mReminderManager = reminderManager;
    }

    public static MovieNotificationManager getInstance(AccountManager accountManager, MovieReminderManager reminderManager) {
        if (mInstance == null) {
            mInstance = new MovieNotificationManager(accountManager, reminderManager);
        }

        return mInstance;
    }

    public void registerNewMovieNotification(Context context, Movie movie) {
        int id = (int) System.currentTimeMillis();

        MovieReminder reminder = new MovieReminder(mAccountManager.getUserId(), movie, id, new Date());
        Intent intent = new Intent(context, MovieReminderReceiver.class);
        intent.putExtra(EXTRA_MOVIE_REMINDER, reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, movie.getReleaseDate().getTime(), pendingIntent);

        mReminderManager.addReminder(reminder);
    }

    public void registerNewMovieNotification(Context context, MovieReminder reminder) {
        int id = reminder.getNotificationId();

        Intent intent = new Intent(context, MovieReminderReceiver.class);
        intent.putExtra(EXTRA_MOVIE_REMINDER, reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getMovie().getReleaseDate().getTime(), pendingIntent);

        mReminderManager.addReminder(reminder);
    }

    public void unregisterNewMovieNotification(Context context, MovieReminder reminder) {
        int id = reminder.getNotificationId();

        Intent intent = new Intent(context, MovieReminderReceiver.class);
        intent.putExtra(EXTRA_MOVIE_REMINDER, reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        mReminderManager.deleteReminder(reminder);
    }
}
