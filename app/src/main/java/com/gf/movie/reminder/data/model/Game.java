package com.gf.movie.reminder.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.gf.movie.reminder.R;

import org.json.JSONObject;

import java.util.Date;

public class Game extends Trailer {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
    private Console mConsole;
    private String mCompany;

    public Game(Parcel in) {
        readFromParcel(in);
    }

    public Game(String json) {
        fromJson(json);
    }

    public Game(String title, String description, String imageUrl, String videoUrl, Date releaseDate, Console console, String company) {
        super(title, description, imageUrl, videoUrl, releaseDate, Type.GAME);
        mConsole = console;
        mCompany = company;
    }

    public Console getConsole() {
        return mConsole;
    }

    public void setConsole(Console console) {
        mConsole = console;
    }

    public String getConsoleName() {
        switch (mConsole) {
            case XBOX:
                return "XBOX";
            case XBOX_360:
                return "XBOX 360";
            case XBOX_ONE:
                return "XBOX ONE";
            case XBOX_STEAM:
                return "XBOX & Steam";
            case XBOX_PC:
                return "XBOX & PC";
            case PLAYSTATION:
                return "Playstation";
            case PS3:
                return "PS3";
            case PS4:
                return "PS3";
            case PLAYSTATION_XBOX:
                return "Playstation & Xbox";
            case PLAYSTATION_STEAM:
                return "Playstation & Steam";
            case PLAYSTATION_PC:
                return "Playstation & PC";
            case STEAM:
                return "Steam";
            case PC:
                return "PC";
            case ALL:
                return "All Major Consoles";
            default:
                return "";
        }
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        mCompany = company;
    }

    public String getReleaseCellString(Context context) {
        return context.getString(R.string.trailers_game_released);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(mConsole.ordinal());
        out.writeString(mCompany);
    }

    @Override
    protected void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mConsole = Console.values()[in.readInt()];
        mCompany = in.readString();
    }

    public void fromJson(String json) {
        try {
            fromJson(new JSONObject(json));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void fromJson(JSONObject json) {
        super.fromJson(json);
        try {
            mConsole = Console.values()[json.getInt("console")];
            mCompany = json.getString("company");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public String toJson() {
        try {
            JSONObject json = toJsonObject();
            json.put("console", mConsole.ordinal());
            json.put("company", mCompany);
            return json.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    public enum Console {
        XBOX,
        XBOX_360,
        XBOX_ONE,
        XBOX_STEAM,
        XBOX_PC,
        PLAYSTATION,
        PS3,
        PS4,
        PLAYSTATION_XBOX,
        PLAYSTATION_STEAM,
        PLAYSTATION_PC,
        STEAM,
        PC,
        ALL
    }
}
