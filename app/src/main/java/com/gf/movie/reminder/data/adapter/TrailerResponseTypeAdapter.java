package com.gf.movie.reminder.data.adapter;

import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.data.model.YoutubeTrailerResponse;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrailerResponseTypeAdapter extends TypeAdapter<YoutubeTrailerResponse> {

    protected TrailerTypeAdapter mAdapter;

    public TrailerResponseTypeAdapter() {
        mAdapter = new TrailerTypeAdapter();
    }

    @Override
    public void write(JsonWriter writer, YoutubeTrailerResponse value) throws IOException {

    }

    @Override
    public YoutubeTrailerResponse read(JsonReader reader) throws IOException {
        List<Trailer> trailers = new ArrayList<Trailer>();
        int totalResults = 0;
        int resultsPerPage = 0;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("pageInfo")) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String nextName = reader.nextName();
                    if (nextName.equals("totalResults")) {
                        totalResults = reader.nextInt();
                    } else if (nextName.equals("resultsPerPage")) {
                        resultsPerPage = reader.nextInt();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else if (name.equals("items")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    Trailer trailer = readTrailer(reader);
                    if (trailer != null) {
                        trailers.add(trailer);
                    }
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new YoutubeTrailerResponse(totalResults, resultsPerPage, trailers);
    }

    private Trailer readTrailer(JsonReader reader) {
        try {
            return mAdapter.read(reader);
        } catch (Throwable t) {
            return null;
        }
    }
}
