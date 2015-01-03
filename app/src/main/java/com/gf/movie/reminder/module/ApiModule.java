package com.gf.movie.reminder.module;

import com.gf.movie.reminder.BuildConfig;
import com.gf.movie.reminder.data.api.ApiHeaders;
import com.gf.movie.reminder.data.api.RequestService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(
        injects = {
        },
        library = true,
        complete = false
)
public class ApiModule {

    @Provides
    @Singleton
    Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }

    @Provides
    @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(BuildConfig.API_URL);
    }

    @Provides
    @Singleton
    RestAdapter.LogLevel provideRetrofitLogLevel() {
        return RestAdapter.LogLevel.FULL;
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, Client client, ApiHeaders headers, RestAdapter.LogLevel logLevel) {
        return new RestAdapter.Builder() //
                .setEndpoint(endpoint) //
                .setClient(client) //
                .setRequestInterceptor(headers) //
                .setConverter(
                        new GsonConverter(
                                new GsonBuilder()
                                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                        .serializeNulls()
                                        .create()))
                .setLogLevel(logLevel)
                .build();
    }

    @Provides
    @Singleton
    RequestService provideRequestService(RestAdapter restAdapter) {
        return restAdapter.create(RequestService.class);
    }
}
