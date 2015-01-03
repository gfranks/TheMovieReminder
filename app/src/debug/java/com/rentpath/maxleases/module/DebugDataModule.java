package com.rentpath.maxleases.module;

import android.content.SharedPreferences;

import com.gf.movie.reminder.BuildConfig;
import com.rentpath.maxleases.data.ApiEndpoint;
import com.rentpath.maxleases.data.ApiEndpoints;
import com.rentpath.maxleases.data.IsMockMode;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.metadude.android.typedpreferences.StringPreference;

@Module(
        includes = {
                DebugApiModule.class,
        },
        complete = false,
        library = true,
        overrides = true
)
public final class DebugDataModule {

    @Provides
    @Singleton
    @ApiEndpoint
    StringPreference provideEndpointPreference(SharedPreferences preferences) {
        return new StringPreference(preferences,
                "debug_endpoint",
                BuildConfig.DEFAULT_ENDPOINT == null ?
                        ApiEndpoints.QA.url :
                        BuildConfig.DEFAULT_ENDPOINT.equals("mock") ?
                                ApiEndpoints.MOCK_MODE.url :
                                ApiEndpoints.QA.url);
    }

    @Provides
    @Singleton
    @IsMockMode
    boolean provideIsMockMode(@ApiEndpoint StringPreference endpoint) {
        return ApiEndpoints.isMockMode(endpoint.get());
    }
}
