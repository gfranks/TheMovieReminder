package com.gf.movie.reminder.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gf.movie.reminder.BuildConfig;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.data.model.Reminder;

import java.util.ArrayList;

public class ReminderManager extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "reminders";
    public static final String KEY_ID = "_id";
    public static final String KEY_USER = "user";
    public static final String KEY_TRAILER = "trailer";
    public static final String KEY_TYPE = "type";
    public static final String KEY_NOTIFICATION_ID = "notificationId";
    public static final String KEY_TIMESTAMP = "timestamp";

    public static final String[] ALL_COLS = new String[]{KEY_ID, KEY_USER, KEY_TRAILER, KEY_NOTIFICATION_ID, KEY_TIMESTAMP};
    // Database creation sql statement
    private final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER + " TEXT," + KEY_TRAILER + " TEXT,"
            + KEY_TYPE + " INTEGER," + KEY_NOTIFICATION_ID + " INTEGER," + KEY_TIMESTAMP + " TEXT" + ")";
    private static final String DATABASE_NAME = "requestCache.mDb";
    private static final int DATABASE_VERSION = 1;
    protected SQLiteDatabase mDb;

    protected ReminderManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase mDb, int oldVersion, int newVersion) {
        Log.w(MovieReminderManager.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(mDb);
    }

    public synchronized SQLiteDatabase getDatabaseInstance() {
        if (mDb == null || !mDb.isOpen()) {
            mDb = super.getWritableDatabase();
        }
        return mDb;
    }

    public synchronized void closeDatabaseInstance() {
        try {
            if (mDb != null) {
                mDb.close();
                mDb = null;
            }
        } catch (Throwable t) {
            mDb = null;
        }
    }

    public synchronized void addReminder(Reminder reminder) {
        if (reminder == null) {
            return;
        }

        mDb = getDatabaseInstance();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER, reminder.getUser());
            values.put(KEY_TRAILER, reminder.getTrailer().toJson());
            values.put(KEY_TYPE, reminder.getTrailer().getType().ordinal());
            values.put(KEY_NOTIFICATION_ID, reminder.getNotificationId());
            values.put(KEY_TIMESTAMP, String.valueOf(reminder.getCreationDate().getTime()));
            mDb.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "Reminder - adding new");
    }

    public synchronized void deleteReminder(Reminder reminder) {
        mDb = getDatabaseInstance();
        try {
            mDb.delete(TABLE_NAME, KEY_USER + "=?" + " AND " + KEY_ID + "=?",
                    new String[]{reminder.getUser(), String.valueOf(reminder.getId())});
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "Reminder - removing");
    }

    public synchronized ArrayList<Reminder> getAll() {
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();

        mDb = getDatabaseInstance();
        Cursor cursor = mDb.query(true, TABLE_NAME, ALL_COLS, KEY_TYPE + "=?",
                new String[]{getType()}, null, null, KEY_TIMESTAMP + " DESC", null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Reminder reminder = getReminderFromCursor(cursor);
                    if (reminder != null) {
                        reminders.add(reminder);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "Reminder - fetching all");

        return reminders;
    }

    public String getType() {
        return "";
    }

    public synchronized void deleteDatabase() {
        mDb = getDatabaseInstance();
        try {
            mDb.delete(TABLE_NAME, null, null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public synchronized Reminder getReminderFromCursor(Cursor cursor) {
        return null;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        throw new IllegalStateException(MovieReminderManager.class.getSimpleName() + " should not directly return a database. Use getDatabaseInstance instead.");
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        throw new IllegalStateException(MovieReminderManager.class.getSimpleName() + " should not directly return a database. Use getDatabaseInstance instead.");
    }
}
