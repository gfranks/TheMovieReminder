package com.gf.movie.reminder.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Reminder implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    private int mId = -1;
    private String mUser;
    private int mNotificationId;
    private Date mCreationDate;
    private Trailer mTrailer;

    public Reminder() {
    }

    public Reminder(Parcel in) {
        readFromParcel(in);
    }

    public Reminder(int id, int notificationId, Date creationDate, Trailer trailer) {
        mId = id;
        mNotificationId = notificationId;
        mCreationDate = creationDate;
        mTrailer = trailer;
    }

    public Reminder(int id, String user, int notificationId, Date creationDate, Trailer trailer) {
        mId = id;
        mUser = user;
        mNotificationId = notificationId;
        mCreationDate = creationDate;
        mTrailer = trailer;
    }

    public Reminder(String user, int notificationId, Date creationDate, Trailer trailer) {
        mUser = user;
        mNotificationId = notificationId;
        mCreationDate = creationDate;
        mTrailer = trailer;
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

    public Trailer getTrailer() {
        return mTrailer;
    }

    public void setTrailer(Trailer trailer) {
        mTrailer = trailer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mId);
        out.writeString(mUser);
        out.writeLong(mCreationDate.getTime());
    }

    protected void readFromParcel(Parcel in) {
        mId = in.readInt();
        mUser = in.readString();
        mCreationDate = new Date(in.readLong());
    }
}
