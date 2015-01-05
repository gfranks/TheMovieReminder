package com.gf.movie.reminder.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class MovieReminder extends Reminder implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieReminder createFromParcel(Parcel in) {
            return new MovieReminder(in);
        }

        public MovieReminder[] newArray(int size) {
            return new MovieReminder[size];
        }
    };

    public MovieReminder(Parcel in) {
        readFromParcel(in);
    }

    public MovieReminder(int id, int notificationId, Date creationDate, Movie movie) {
        super(id, notificationId, creationDate, movie);
    }

    public MovieReminder(int id, String user, int notificationId, Date creationDate, Movie movie) {
        super(id, user, notificationId, creationDate, movie);
    }

    public MovieReminder(String user, int notificationId, Date creationDate, Movie movie) {
        super(user, notificationId, creationDate, movie);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        getTrailer().writeToParcel(out, flags);
    }

    @Override
    protected void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        setTrailer(new Movie(in));
    }
}
