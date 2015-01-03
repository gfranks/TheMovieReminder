package com.rentpath.maxleases.data;

import com.gf.movie.reminder.BuildConfig;

public enum ApiEndpoints {
    //It's okay to hardcode endpoints here, as this enum is
    //DebugApiModule, which is only injected in Debug builds.
    PRODUCTION("Production", "http://www.google.com"),
    //BuildConfig.API_URL == QA as we are in Debug build.
    QA("QA", BuildConfig.API_URL),
    MOCK_MODE("Mock Mode", "mock://"),
    CUSTOM("Custom", null);

    public final String name;
    public final String url;

    ApiEndpoints(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static ApiEndpoints from(String endpoint) {
        for (int i = 0; i < values().length; i++) {
            ApiEndpoints value = values()[i];
            if (value.url != null && value.url.equals(endpoint)) {
                return value;
            }
        }
        return CUSTOM;
    }

    public static boolean isMockMode(String endpoint) {
        return from(endpoint) == MOCK_MODE;
    }

    @Override
    public String toString() {
        return name;
    }
}
