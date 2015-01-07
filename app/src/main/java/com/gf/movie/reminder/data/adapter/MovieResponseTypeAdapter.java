package com.gf.movie.reminder.data.adapter;

import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.YoutubeMovieResponse;
import com.gf.movie.reminder.data.model.YoutubeTrailerResponse;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieResponseTypeAdapter extends TrailerResponseTypeAdapter {

    public MovieResponseTypeAdapter() {
        mAdapter = new MovieTypeAdapter();
    }

    @Override
    public void write(JsonWriter writer, YoutubeTrailerResponse value) throws IOException {
        super.write(writer, value);
    }

    @Override
    public YoutubeTrailerResponse read(JsonReader reader) throws IOException {
        List<Movie> movies = new ArrayList<Movie>();
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
                    Movie movie = readMovie(reader);
                    if (movie != null) {
                        movies.add(movie);
                    }
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new YoutubeMovieResponse(totalResults, resultsPerPage, movies);
    }

    private Movie readMovie(JsonReader reader) {
        try {
            return (Movie) mAdapter.read(reader);
        } catch (Throwable t) {
            return null;
        }
    }
}
