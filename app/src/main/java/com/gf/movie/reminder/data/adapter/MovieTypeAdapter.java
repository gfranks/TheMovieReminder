package com.gf.movie.reminder.data.adapter;

import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.Trailer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class MovieTypeAdapter extends TrailerTypeAdapter {

    @Override
    public void write(JsonWriter writer, Trailer value) throws IOException {
        super.write(writer, value);
    }

    @Override
    public Trailer read(JsonReader reader) throws IOException {
        Trailer trailer = super.read(reader);
        Movie movie = new Movie();
        movie.setTitle(trailer.getTitle());
        movie.setDescription(trailer.getDescription());
        movie.setImageUrl(trailer.getImageUrl());
        movie.setVideoUrl(trailer.getVideoUrl());
        movie.setReleaseDate(trailer.getReleaseDate());
        movie.setType(Trailer.Type.MOVIE);
        return movie;
    }
}
