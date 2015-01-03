package com.rentpath.maxleases.module;

import com.rentpath.maxleases.application.MaxLeasesApplication;

public final class Modules {

    private Modules() {
        // No instances.
    }

    public static Object[] list(MaxLeasesApplication app) {
        return new Object[]{
                new MaxLeasesModule(app)
        };
    }
}