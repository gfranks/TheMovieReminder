package com.gf.movie.reminder.data.adapter;

import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.YoutubeGameResponse;
import com.gf.movie.reminder.data.model.YoutubeTrailerResponse;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameResponseTypeAdapter extends TrailerResponseTypeAdapter {

    public GameResponseTypeAdapter() {
        mAdapter = new GameTypeAdapter();
    }

    @Override
    public void write(JsonWriter writer, YoutubeTrailerResponse value) throws IOException {
        super.write(writer, value);
    }

    @Override
    public YoutubeTrailerResponse read(JsonReader reader) throws IOException {
        List<Game> games = new ArrayList<Game>();
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
                    Game game = readGame(reader);
                    if (game != null) {
                        games.add(game);
                    }
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new YoutubeGameResponse(totalResults, resultsPerPage, games);
    }

    private Game readGame(JsonReader reader) {
        try {
            return (Game) mAdapter.read(reader);
        } catch (Throwable t) {
            return null;
        }
    }
}
