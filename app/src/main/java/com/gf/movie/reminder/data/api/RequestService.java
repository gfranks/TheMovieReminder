package com.gf.movie.reminder.data.api;

import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminderSession;
import com.gf.movie.reminder.data.model.Trailer;

import java.util.ArrayList;
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

    @GET("/search?part=snippet&type=video&videoCaption=closedCaption")
    void search(@Query("key") String apiKey, @Query("q") String query, Callback<ArrayList<Trailer>> cb);

    @GET("/search?part=snippet&q=movie%20trailers&type=video&videoCaption=closedCaption")
    void getMovieTrailers(@Query("key") String apiKey, Callback<ArrayList<Movie>> cb);

    @GET("/search?part=snippet&q=video%20game%20trailers&type=video&videoCaption=closedCaption")
    void getGameTrailers(@Query("key") String apiKey, Callback<ArrayList<Game>> cb);

//    @POST("/api/auth/exchange/")
//    void exchangeSession( //
//                          @Body HashMap body, //
//                          Callback<MaxLeasesSession> cb);
//
//    @POST("/api/auth/exchange/")
//    MaxLeasesSession exchangeSession( //
//                                   @Body HashMap body);
//
//    @POST("/api/auth/")
//    void login( //
//                @Body HashMap credentials, //
//                Callback<MaxLeasesSession> cb);
//
//    @POST("/api/auth/forgot_password/")
//    void forgotPassword( //
//                         @Body HashMap body, //
//                         Callback<Void> cb);
//
//    @GET("/api/users/{userId}/")
//    void getUser( //
//                  @Path("userId") String userId, //
//                  Callback<MaxLeasesUser> cb);
//
//    @GET("/api/users/{userId}/")
//    MaxLeasesUser getUser( //
//                        @Path("userId") String userId);
//
//    @POST("/api/users/")
//    void createUser( //
//                     @Body HashMap params, //
//                     Callback<MaxLeasesUser> cb);
//
//    @GET("/api/users/{userId}/alerts/")
//    void getReminders( //
//                    @Path("userId") String userId, //
//                    @Query("page") int page,
//                    Callback<LovelyResultPage<MaxLeasesReminder>> cb);
//
//    @GET("/api/users/{userId}/alerts/{id}/")
//    void getReminders( //
//                   @Path("userId") String userId, //
//                   @Path("id") String id, //
//                   Callback<MaxLeasesReminder> cb);
//
//    @POST("/api/users/{userId}/alerts/")
//    void createReminder( //
//                      @Path("userId") String userId, //
//                      @Body Map<String, Object> alert, //
//                      Callback<MaxLeasesReminder> cb);
//
//    @PATCH("/api/users/{userId}/alerts/{id}/")
//    void updateReminder( //
//                      @Path("userId") String userId, //
//                      @Path("id") String id, //
//                      @Body Map<String, Object> alert, //
//                      Callback<MaxLeasesReminder> cb);
//
//    @DELETE("/api/users/{userId}/alerts/{id}/")
//    void deleteReminder( //
//                      @Path("userId") String userId, //
//                      @Path("id") String id, //
//                      Callback<MaxLeasesReminder> cb);
}
