package com.gf.movie.reminder.util;

import android.app.Application;
import android.content.SharedPreferences;

import com.gf.movie.reminder.data.model.MovieReminderSession;

import java.util.Date;

import info.metadude.android.typedpreferences.BooleanPreference;
import info.metadude.android.typedpreferences.StringPreference;

public class AccountManager {

    public static final String USER_ID_NOT_SET = "-1";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_NEW_REMINDERS = "has_new_reminders";
    private static final String DEFAULT_FIRST_NAME = "";
    private static final String DEFAULT_LAST_NAME = "";
    private static final String DEFAULT_EMAIL = "";
    protected SharedPreferences mAccountSharedPreferences;

    protected StringPreference mUserIdStringPreference;
    protected StringPreference mFirstNameStringPreference;
    protected StringPreference mLastNameStringPreference;
    protected StringPreference mEmailStringPreference;
    protected StringPreference mAuthTokenStringPreference;

    protected BooleanPreference mNewRemindersBooleanPreference;
    protected BooleanPreference mLoggedInBooleanPreference;

    public AccountManager(SharedPreferences prefs, Application app) {
        mAccountSharedPreferences = prefs;
        mFirstNameStringPreference = new StringPreference(prefs, KEY_FIRST_NAME, DEFAULT_FIRST_NAME);
        mLastNameStringPreference = new StringPreference(prefs, KEY_LAST_NAME, DEFAULT_LAST_NAME);
        mEmailStringPreference = new StringPreference(prefs, KEY_EMAIL, DEFAULT_EMAIL);
        mAuthTokenStringPreference = new StringPreference(prefs, KEY_TOKEN, null);
        mUserIdStringPreference = new StringPreference(prefs, KEY_USER_ID, USER_ID_NOT_SET);
        mNewRemindersBooleanPreference = new BooleanPreference(prefs, KEY_NEW_REMINDERS, false);
        mLoggedInBooleanPreference = new BooleanPreference(prefs, KEY_LOGGED_IN, false);

    }

    public boolean hasNewReminder() {
        return mNewRemindersBooleanPreference.get();
    }

    public void setHasNewReminders(boolean hasNewListings) {
        mNewRemindersBooleanPreference.set(hasNewListings);
    }

    public boolean isLoggedIn() {
        return mLoggedInBooleanPreference.get();
    }

    public String getUserId() {
        String id = mUserIdStringPreference.get();
        if (id == null) {
            id = String.valueOf(new Date().getTime());
        }

        return id;
    }

    public String getAuthToken() {
        return mAuthTokenStringPreference.get();
    }

    public void newSession(String token) {
        mAuthTokenStringPreference.set(token);
    }

    public void newSession(String token, String userId) {
        mUserIdStringPreference.set(userId);
        mAuthTokenStringPreference.set(token);
    }

    public void login(String userId, String token) {
        mUserIdStringPreference.set(userId);
        mAuthTokenStringPreference.set(token);
        mLoggedInBooleanPreference.set(true);
    }

    public void login(MovieReminderSession session) {
        mUserIdStringPreference.set(session.getUserId());
        mAuthTokenStringPreference.set(session.getToken());
        mFirstNameStringPreference.set(session.getFirstname());
        mLastNameStringPreference.set(session.getLastname());
        mEmailStringPreference.set(session.getEmail());
        mLoggedInBooleanPreference.set(true);
    }

    public void logout() {
        mUserIdStringPreference.delete();
        mAuthTokenStringPreference.delete();
        mLoggedInBooleanPreference.set(false);
    }
}
