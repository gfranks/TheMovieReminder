package com.gf.movie.reminder.data.api;

import com.gf.movie.reminder.data.model.MovieReminderSession;
import com.gf.movie.reminder.data.model.YoutubeGameResponse;
import com.gf.movie.reminder.data.model.YoutubeMovieResponse;
import com.gf.movie.reminder.data.model.YoutubeTrailerResponse;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface RequestService {

    @POST("/login")
    void login(@Body HashMap creds,
               Callback<MovieReminderSession> cb);

    @POST("/register")
    void register(@Body HashMap creds,
                  Callback<MovieReminderSession> cb);

    @GET("/search?maxResults=25&part=snippet&type=video&videoCaption=closedCaption")
    void search(@Query("key") String apiKey, @Query("q") String query, Callback<YoutubeTrailerResponse> cb);

    @GET("/search?maxResults=25&part=snippet&q=movie%20trailers&type=video&videoCaption=closedCaption")
    void getMovieTrailers(@Query("key") String apiKey, Callback<YoutubeMovieResponse> cb);

    @GET("/search?maxResults=25&part=snippet&q=video%20game%20trailers&type=video&videoCaption=closedCaption")
    void getGameTrailers(@Query("key") String apiKey, Callback<YoutubeGameResponse> cb);
}