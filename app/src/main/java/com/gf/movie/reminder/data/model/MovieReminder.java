package com.gf.movie.reminder.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class MovieReminder implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieReminder createFromParcel(Parcel in) {
            return new MovieReminder(in);
        }

        public MovieReminder[] newArray(int size) {
            return new MovieReminder[size];
        }
    };

    private int mId = -1;
    private String mUser;
    private Movie mMovie;
    private int mNotificationId;
    private Date mCreationDate;

    public MovieReminder(Parcel in) {
        readFromParcel(in);
    }

    public MovieReminder(int id, Movie movie, int notificationId, Date creationDate) {
        mId = id;
        mMovie = movie;
        mNotificationId = notificationId;
        mCreationDate = creationDate;
    }

    public MovieReminder(int id, String user, Movie movie, int notificationId, Date creationDate) {
        mId = id;
        mUser = user;
        mMovie = movie;
        mNotificationId = notificationId;
        mCreationDate = creationDate;
    }

    public MovieReminder(String user, Movie movie, int notificationId, Date creationDate) {
        mUser = user;
        mMovie = movie;
        mNotificationId = notificationId;
        mCreationDate = creationDate;
    }

    public long getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
    }

    public int getNotificationId() {
        return mNotificationId;
    }

    public void setNotificationId(int notificationId) {
        mNotificationId = notificationId;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.mCreationDate = creationDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mId);
        out.writeString(mUser);
        mMovie.writeToParcel(out, flags);
        out.writeLong(mCreationDate.getTime());
    }

    private void readFromParcel(Parcel in) {
        mId = in.readInt();
        mUser = in.readString();
        mMovie = new Movie(in);
        mCreationDate = new Date(in.readLong());
    }
}
