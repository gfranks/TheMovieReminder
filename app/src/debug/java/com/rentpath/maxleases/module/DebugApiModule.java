package com.rentpath.maxleases.module;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.gf.movie.reminder.data.adapter.GameResponseTypeAdapter;
import com.gf.movie.reminder.data.adapter.MovieResponseTypeAdapter;
import com.gf.movie.reminder.data.adapter.TrailerResponseTypeAdapter;
import com.gf.movie.reminder.data.api.ApiHeaders;
import com.gf.movie.reminder.data.api.RequestService;
import com.gf.movie.reminder.data.model.YoutubeGameResponse;
import com.gf.movie.reminder.data.model.YoutubeMovieResponse;
import com.gf.movie.reminder.data.model.YoutubeTrailerResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.rentpath.maxleases.data.ApiEndpoint;
import com.rentpath.maxleases.data.IsMockMode;
import com.rentpath.maxleases.data.api.MockRequestService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.metadude.android.typedpreferences.StringPreference;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import retrofit.android.AndroidMockValuePersistence;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;

@Module(
        library = true,
        overrides = true,
        complete = false
)
public final class DebugApiModule {

    @Provides
    @Singleton
    RestAdapter.LogLevel provideRetrofitLogLevel() {
        return RestAdapter.LogLevel.FULL;
    }

    @Provides
    @Singleton
    MockRestAdapter provideMockRestAdapter(RestAdapter restAdapter, SharedPreferences preferences) {
        MockRestAdapter mockRestAdapter = MockRestAdapter.from(restAdapter);
        AndroidMockValuePersistence.install(mockRestAdapter, preferences);
        mockRestAdapter.setDelay(0);
        mockRestAdapter.setErrorPercentage(0);
        mockRestAdapter.setVariancePercentage(0);
        return mockRestAdapter;
    }

    @Provides
    @Singleton
    Endpoint provideEndpoint(@ApiEndpoint StringPreference endpoint) {
        return Endpoints.newFixedEndpoint(endpoint.get());
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, Client client,
                                   ApiHeaders headers, RestAdapter.LogLevel logLevel,
                                   @IsMockMode boolean isMockMode) {

        RestAdapter.Builder builder = new RestAdapter.Builder() //
                .setEndpoint(endpoint) //
                .setClient(client) //
                .setRequestInterceptor(headers) //
                .setConverter(
                        new GsonConverter(
                                new GsonBuilder()
                                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                        .serializeNulls()
                                        .registerTypeAdapter(YoutubeTrailerResponse.class, new TrailerResponseTypeAdapter())
                                        .registerTypeAdapter(YoutubeMovieResponse.class, new MovieResponseTypeAdapter())
                                        .registerTypeAdapter(YoutubeGameResponse.class, new GameResponseTypeAdapter())
                                        .create()))
                .setLogLevel(logLevel);
        if (isMockMode) {
            builder.setExecutors(AsyncTask.THREAD_POOL_EXECUTOR, new MainThreadExecutor());
        }

        return builder.build();
    }

    @Provides
    @Singleton
    RequestService provideRequestService(RestAdapter restAdapter, MockRestAdapter mockRestAdapter,
                                         @IsMockMode boolean isMockMode, MockRequestService mockService) {
        if (isMockMode) {
            return mockRestAdapter.create(RequestService.class, mockService);
        }
        return restAdapter.create(RequestService.class);
    }
}