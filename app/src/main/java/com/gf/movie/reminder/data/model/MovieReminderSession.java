package com.gf.movie.reminder.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReminderSession implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieReminderSession createFromParcel(Parcel in) {
            return new MovieReminderSession(in);
        }

        public MovieReminderSession[] newArray(int size) {
            return new MovieReminderSession[size];
        }
    };
    private String mToken;
    private String mUserId;
    private String mFirstname;
    private String mLastname;
    private String mEmail;

    public MovieReminderSession() {
    }

    public MovieReminderSession(Parcel in) {
        readFromParcel(in);
    }

    public MovieReminderSession(String token) {
        this.mToken = token;
    }

    public MovieReminderSession(String token, String userId, String email) {
        this.mToken = token;
        this.mUserId = userId;
        this.mEmail = email;
    }


    public MovieReminderSession(String token, String userId, String email, String firstname, String lastname) {
        this.mToken = token;
        this.mUserId = userId;
        this.mEmail = email;
        this.mFirstname = firstname;
        this.mLastname = lastname;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String firstname) {
        this.mFirstname = firstname;
    }

    public String getLastname() {
        return mLastname;
    }

    public void setLastname(String lastname) {
        this.mLastname = lastname;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mToken);
        out.writeString(mUserId);
        out.writeString(mFirstname);
        out.writeString(mLastname);
        out.writeString(mEmail);
    }

    private void readFromParcel(Parcel in) {
        mToken = in.readString();
        mUserId = in.readString();
        mFirstname = in.readString();
        mLastname = in.readString();
        mEmail = in.readString();
    }
}
