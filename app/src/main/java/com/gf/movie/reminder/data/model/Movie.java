package com.gf.movie.reminder.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.gf.movie.reminder.R;

import org.json.JSONObject;

import java.util.Date;

public class Movie extends Trailer {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String mDirectors;
    private String mStars;

    public Movie(Parcel in) {
        readFromParcel(in);
    }

    public Movie(String json) {
        fromJson(json);
    }

    public Movie(String title, String description, String imageUrl, String videoUrl, Date releaseDate, String directors, String stars) {
        super(title, description, imageUrl, videoUrl, releaseDate, Type.MOVIE);
        mDirectors = directors;
        mStars = stars;
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

    public boolean isOnDVDOrBluRay() {
        return false;
    }

    public String getReleaseCellString(Context context) {
        return context.getString(R.string.trailers_movie_in_theaters);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(mDirectors);
        out.writeString(mStars);
    }

    @Override
    protected void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mDirectors = in.readString();
        mStars = in.readString();
    }

    @Override
    public void fromJson(JSONObject json) {
        super.fromJson(json);
        try {
            mDirectors = json.getString("directors");
            mStars = json.getString("stars");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public String toJson() {
        try {
            JSONObject json = toJsonObject();
            json.put("directors", mDirectors);
            json.put("stars", mStars);
            return json.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }
}
