package com.gf.movie.reminder.data.model;

import java.util.List;

public class YoutubeGameResponse extends YoutubeTrailerResponse {

    public YoutubeGameResponse() {}
    public YoutubeGameResponse(int totalResults, int resultsPerPage, List<Game> games) {
        super(totalResults, resultsPerPage, games);
    }
}
