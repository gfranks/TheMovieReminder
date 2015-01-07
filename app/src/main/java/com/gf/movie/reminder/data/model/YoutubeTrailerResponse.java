package com.gf.movie.reminder.data.model;

import java.util.List;

public class YoutubeTrailerResponse {

    private int mTotalResults;
    private int mResultsPerPage;
    private List<? extends Trailer> mTrailers;

    public YoutubeTrailerResponse() {
    }

    public YoutubeTrailerResponse(int totalResults, int resultsPerPage, List<? extends Trailer> trailers) {
        mTotalResults = totalResults;
        mResultsPerPage = resultsPerPage;
        mTrailers = trailers;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        mTotalResults = totalResults;
    }

    public int getResultsPerPage() {
        return mResultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        mResultsPerPage = resultsPerPage;
    }

    public List<? extends Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<? extends Trailer> trailers) {
        mTrailers = trailers;
    }
}
