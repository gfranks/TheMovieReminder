package com.gf.movie.reminder.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class GameReminder extends Reminder implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public GameReminder createFromParcel(Parcel in) {
            return new GameReminder(in);
        }

        public GameReminder[] newArray(int size) {
            return new GameReminder[size];
        }
    };

    public GameReminder(Parcel in) {
        readFromParcel(in);
    }

    public GameReminder(int id, int notificationId, Date creationDate, Game game) {
        super(id, notificationId, creationDate, game);
    }

    public GameReminder(int id, String user, int notificationId, Date creationDate, Game game) {
        super(id, user, notificationId, creationDate, game);
    }

    public GameReminder(String user, int notificationId, Date creationDate, Game game) {
        super(user, notificationId, creationDate, game);
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
        setTrailer(new Game(in));
    }
}
