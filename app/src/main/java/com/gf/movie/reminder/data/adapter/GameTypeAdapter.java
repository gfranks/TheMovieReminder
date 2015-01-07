package com.gf.movie.reminder.data.adapter;

import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.Trailer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GameTypeAdapter extends TrailerTypeAdapter {

    @Override
    public void write(JsonWriter writer, Trailer value) throws IOException {
        super.write(writer, value);
    }

    @Override
    public Trailer read(JsonReader reader) throws IOException {
        Trailer trailer = super.read(reader);
        Game game = new Game();
        game.setTitle(trailer.getTitle());
        game.setDescription(trailer.getDescription());
        game.setImageUrl(trailer.getImageUrl());
        game.setVideoUrl(trailer.getVideoUrl());
        game.setReleaseDate(trailer.getReleaseDate());
        game.setType(Trailer.Type.GAME);
        return game;
    }
}