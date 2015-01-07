package com.gf.movie.reminder.data.adapter;

import com.gf.movie.reminder.data.model.Trailer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class TrailerTypeAdapter extends TypeAdapter<Trailer> {

    @Override
    public void write(JsonWriter writer, Trailer value) throws IOException {

    }

    @Override
    public Trailer read(JsonReader reader) throws IOException {
        Trailer trailer = new Trailer();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String nextName = reader.nextName();
                    if (nextName.equals("videoId")) {
                        trailer.setVideoUrl("https://www.youtube.com/watch?v=" + reader.nextString());
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else if (name.equals("snippet")) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String nextName = reader.nextName();
                    if (nextName.equals("title")) {
                        trailer.setTitle(reader.nextString());
                    } else if (nextName.equals("description")) {
                        trailer.setDescription(reader.nextString());
                    } else if (nextName.equals("thumbnails")) {
                        reader.beginObject();
                        while (reader.hasNext()) {
                            String lastName = reader.nextName();
                            if (lastName.equals("high")) {
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    String finalName = reader.nextName();
                                    if (finalName.equals("url")) {
                                        trailer.setImageUrl(reader.nextString());
                                    } else {
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return trailer;
    }
}
