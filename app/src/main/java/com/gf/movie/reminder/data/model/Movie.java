package com.gf.movie.reminder.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Movie implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String mTitle;
    private String mDescription;
    private String mImageUrl;
    private String mVideoUrl;
    private String mDirectors;
    private String mStars;
    private Date mReleaseDate;

    public Movie(Parcel in) {
        readFromParcel(in);
    }

    public Movie(String json) {
        fromJson(json);
    }

    public Movie(String title, String description, String imageUrl, String videoUrl, String directors, String stars, Date releaseDate) {
        mTitle = title;
        mDescription = description;
        mImageUrl = imageUrl;
        mVideoUrl = videoUrl;
        mDirectors = directors;
        mStars = stars;
        mReleaseDate = releaseDate;
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

    public String getDirectors() {
        return mDirectors;
    }

    public void setDirectors(String directors) {
        this.mDirectors = directors;
    }

    public String getStars() {
        return mStars;
    }

    public void setStars(String stars) {
        this.mStars = stars;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public String getTitleString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return mTitle + " (" + format.format(mReleaseDate) + ")";
    }

    public String getReleaseDateString() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(mReleaseDate);
    }

    public boolean isInTheaters() {
        return mReleaseDate.before(new Date());
    }

    public boolean isOnDVDOrBluRay() {
        return false;
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
        out.writeString(mDirectors);
        out.writeString(mStars);
        out.writeLong(mReleaseDate.getTime());
    }

    private void readFromParcel(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        mImageUrl = in.readString();
        mVideoUrl = in.readString();
        mDirectors = in.readString();
        mStars = in.readString();
        mReleaseDate = new Date(in.readLong());
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
            mDirectors = json.getString("directors");
            mStars = json.getString("stars");
            mReleaseDate = new Date(json.getLong("release_date"));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String toJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("title", mTitle);
            json.put("description", mDescription);
            json.put("image_url", mImageUrl);
            json.put("video_url", mVideoUrl);
            json.put("directors", mDirectors);
            json.put("stars", mStars);
            json.put("release_date", mReleaseDate.getTime());
            return json.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }
}
