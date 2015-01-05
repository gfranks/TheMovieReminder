package com.gf.movie.reminder.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gf.movie.reminder.BuildConfig;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminder;

import java.util.ArrayList;
import java.util.Date;

public class MovieReminderManager extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "reminders";
    public static final String KEY_ID = "_id";
    public static final String KEY_USER = "user";
    public static final String KEY_MOVIE = "movie";
    public static final String KEY_NOTIFICATION_ID = "notificationId";
    public static final String KEY_TIMESTAMP = "timestamp";

    public static final String[] ALL_COLS = new String[]{KEY_ID, KEY_USER, KEY_MOVIE, KEY_NOTIFICATION_ID, KEY_TIMESTAMP};
    // Database creation sql statement
    private final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER + " TEXT,"
            + KEY_MOVIE + " TEXT," + KEY_NOTIFICATION_ID + " INTEGER," + KEY_TIMESTAMP + " TEXT" + ")";
    private static final String DATABASE_NAME = "requestCache.mDb";
    private static final int DATABASE_VERSION = 1;
    private static MovieReminderManager mInstance;
    private SQLiteDatabase mDb;

    private MovieReminderManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized void initializeInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MovieReminderManager(context);
        } else {
            throw new IllegalStateException(MovieReminderManager.class.getSimpleName() + " was already initialized.");
        }
    }

    public static MovieReminderManager getInstance(Context context) {
        if (mInstance == null) {
            initializeInstance(context);
        }
        return mInstance;
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

    public synchronized void addReminder(MovieReminder reminder) {
        if (reminder == null) {
            return;
        }

        mDb = getDatabaseInstance();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER, reminder.getUser());
            values.put(KEY_MOVIE, reminder.getMovie().toJson());
            values.put(KEY_NOTIFICATION_ID, reminder.getNotificationId());
            values.put(KEY_TIMESTAMP, String.valueOf(reminder.getCreationDate().getTime()));
            mDb.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "Reminder - adding new");
    }

    public synchronized void deleteReminder(MovieReminder reminder) {
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

    public synchronized ArrayList<MovieReminder> getAll() {
        ArrayList<MovieReminder> reminders = new ArrayList<MovieReminder>();

        mDb = getDatabaseInstance();
        Cursor cursor = mDb.query(true, TABLE_NAME, ALL_COLS, null, null, null, null, KEY_TIMESTAMP + " DESC", null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MovieReminder reminder = getReminderFromCursor(cursor);
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

    public synchronized void deleteDatabase() {
        mDb = getDatabaseInstance();
        try {
            mDb.delete(TABLE_NAME, null, null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public synchronized MovieReminder getReminderFromCursor(Cursor cursor) {
        MovieReminder reminder = null;
        try {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int userIndex = cursor.getColumnIndex(KEY_USER);
            int urlIndex = cursor.getColumnIndex(KEY_MOVIE);
            int notificationIdIndex = cursor.getColumnIndex(KEY_NOTIFICATION_ID);
            int timestampIndex = cursor.getColumnIndex(KEY_TIMESTAMP);
            int id = cursor.getInt(idIndex);
            String user = cursor.getString(userIndex);
            String movieJson = cursor.getString(urlIndex);
            int notificationId = cursor.getInt(notificationIdIndex);
            long timestamp = Long.parseLong(cursor.getString(timestampIndex));
            reminder = new MovieReminder(id, user, new Movie(movieJson), notificationId, new Date(timestamp));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reminder;
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
