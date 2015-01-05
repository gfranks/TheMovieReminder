package com.gf.movie.reminder.util;

import android.content.Context;
import android.database.Cursor;

import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.GameReminder;
import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.data.model.Trailer;

import java.util.Date;

public class GameReminderManager extends ReminderManager {

    private static GameReminderManager mInstance;

    protected GameReminderManager(Context context) {
        super(context);
    }

    public static synchronized void initializeInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GameReminderManager(context);
        } else {
            throw new IllegalStateException(GameReminderManager.class.getSimpleName() + " was already initialized.");
        }
    }

    public static GameReminderManager getInstance(Context context) {
        if (mInstance == null) {
            initializeInstance(context);
        }
        return mInstance;
    }

    @Override
    public String getType() {
        return String.valueOf(Trailer.Type.GAME.ordinal());
    }

    @Override
    public synchronized Reminder getReminderFromCursor(Cursor cursor) {
        Reminder reminder = null;
        try {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int userIndex = cursor.getColumnIndex(KEY_USER);
            int urlIndex = cursor.getColumnIndex(KEY_TRAILER);
            int notificationIdIndex = cursor.getColumnIndex(KEY_NOTIFICATION_ID);
            int timestampIndex = cursor.getColumnIndex(KEY_TIMESTAMP);
            int id = cursor.getInt(idIndex);
            String user = cursor.getString(userIndex);
            String movieJson = cursor.getString(urlIndex);
            int notificationId = cursor.getInt(notificationIdIndex);
            long timestamp = Long.parseLong(cursor.getString(timestampIndex));
            reminder = new GameReminder(id, user, notificationId, new Date(timestamp), new Game(movieJson));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reminder;
    }
}
