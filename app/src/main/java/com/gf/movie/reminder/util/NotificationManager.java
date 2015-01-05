package com.gf.movie.reminder.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.GameReminder;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminder;

import java.util.Date;

public class NotificationManager {

    public static final String EXTRA_MOVIE_REMINDER = "reminder";

    private static NotificationManager mInstance;

    private AccountManager mAccountManager;
    private MovieReminderManager mReminderManager;

    private NotificationManager(AccountManager accountManager, MovieReminderManager reminderManager) {
        mAccountManager = accountManager;
        mReminderManager = reminderManager;
    }

    public static NotificationManager getInstance(AccountManager accountManager, MovieReminderManager reminderManager) {
        if (mInstance == null) {
            mInstance = new NotificationManager(accountManager, reminderManager);
        }

        return mInstance;
    }

    public void registerNewMovieNotification(Context context, Movie movie) {
        int id = (int) System.currentTimeMillis();

        MovieReminder reminder = new MovieReminder(mAccountManager.getUserId(), id, new Date(), movie);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MOVIE_REMINDER, reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, movie.getReleaseDate().getTime(), pendingIntent);

        mReminderManager.addReminder(reminder);
    }

    public void registerNewMovieNotification(Context context, MovieReminder reminder) {
        int id = reminder.getNotificationId();

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MOVIE_REMINDER, reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getTrailer().getReleaseDate().getTime(), pendingIntent);

        mReminderManager.addReminder(reminder);
    }

    public void unregisterNewMovieNotification(Context context, MovieReminder reminder) {
        int id = reminder.getNotificationId();

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MOVIE_REMINDER, reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        mReminderManager.deleteReminder(reminder);
    }

    public void registerNewGameNotification(Context context, Game game) {
        int id = (int) System.currentTimeMillis();

        GameReminder reminder = new GameReminder(mAccountManager.getUserId(), id, new Date(), game);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MOVIE_REMINDER, reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, game.getReleaseDate().getTime(), pendingIntent);

        mReminderManager.addReminder(reminder);
    }

    public void registerNewGameNotification(Context context, GameReminder reminder) {
        int id = reminder.getNotificationId();

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MOVIE_REMINDER, reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getTrailer().getReleaseDate().getTime(), pendingIntent);

        mReminderManager.addReminder(reminder);
    }

    public void unregisterNewGameNotification(Context context, GameReminder reminder) {
        int id = reminder.getNotificationId();

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MOVIE_REMINDER, reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        mReminderManager.deleteReminder(reminder);
    }
}
