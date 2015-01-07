package com.gf.movie.reminder.data.model;

import java.util.List;

public class YoutubeMovieResponse extends YoutubeTrailerResponse {

    public YoutubeMovieResponse() {}
    public YoutubeMovieResponse(int totalResults, int resultsPerPage, List<Movie> movies) {
        super(totalResults, resultsPerPage, movies);
    }
}
