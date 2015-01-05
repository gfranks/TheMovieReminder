package com.gf.movie.reminder.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Trailer implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    private String mTitle;
    private String mDescription;
    private String mImageUrl;
    private String mVideoUrl;
    private Date mReleaseDate;
    private Type mType;
    public Trailer() {
    }

    public Trailer(String json) {
        fromJson(json);
    }

    public Trailer(Parcel in) {
        readFromParcel(in);
    }

    public Trailer(String title, String description, String imageUrl, String videoUrl, Date releaseDate, Type type) {
        mTitle = title;
        mDescription = description;
        mImageUrl = imageUrl;
        mVideoUrl = videoUrl;
        mReleaseDate = releaseDate;
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.mVideoUrl = videoUrl;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public String getTitleString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return mTitle + " (" + format.format(mReleaseDate) + ")";
    }

    public String getReleaseDateString() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(mReleaseDate);
    }

    public String getReleaseCellString(Context context) {
        return "";
    }

    public boolean isReleased() {
        return getReleaseDate().before(new Date());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mDescription);
        out.writeString(mImageUrl);
        out.writeString(mVideoUrl);
        out.writeLong(mReleaseDate.getTime());
        out.writeInt(mType.ordinal());
    }

    protected void readFromParcel(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        mImageUrl = in.readString();
        mVideoUrl = in.readString();
        mReleaseDate = new Date(in.readLong());
        mType = Type.values()[in.readInt()];
    }

    public void fromJson(String json) {
        try {
            fromJson(new JSONObject(json));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void fromJson(JSONObject json) {
        try {
            mTitle = json.getString("title");
            mDescription = json.getString("description");
            mImageUrl = json.getString("image_url");
            mVideoUrl = json.getString("video_url");
            mReleaseDate = new Date(json.getLong("release_date"));
            mType = Type.values()[json.getInt("type")];
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String toJson() {
        return null;
    }

    protected JSONObject toJsonObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("title", getTitle());
        json.put("description", getDescription());
        json.put("image_url", getImageUrl());
        json.put("video_url", getVideoUrl());
        json.put("release_date", getReleaseDate().getTime());
        json.put("type", mType.ordinal());
        return json;
    }

    public enum Type {
        GAME,
        MOVIE;
    }
}
