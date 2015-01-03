package com.gf.movie.reminder.data.api;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.gf.movie.reminder.BuildConfig;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.util.AccountManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.RequestInterceptor;

@Singleton
public final class ApiHeaders implements RequestInterceptor {

    private static final String HEADER_TYPE = "Device-Type";
    private static final String HEADER_VERSION = "App-Version";
    private static final String API_VERSION = "Api-Version";
    private static String ANDROID_VERSION;

    private final AccountManager mAccountManager;
    private final ConnectivityManager mConnectivityManager;

    @Inject
    public ApiHeaders(Application app, AccountManager accountManager) {
        mAccountManager = accountManager;
        ANDROID_VERSION = "";
        try {
            ANDROID_VERSION = String.valueOf(app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
        }
        mConnectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void intercept(RequestFacade request) {
        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "intercept Request for Headers");
        request.addHeader(HEADER_TYPE, "android");
        request.addHeader(HEADER_VERSION, ANDROID_VERSION);
        request.addHeader(API_VERSION, "1");
//        if (mAccountManager.isAuthenticated()) {
//            request.addHeader("Authorization", "Token " + mAccountManager.getAuthToken());
//        }
        if (mConnectivityManager != null) {
            NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && !activeNetworkInfo.isConnected()) {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                request.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
        }
    }
}
