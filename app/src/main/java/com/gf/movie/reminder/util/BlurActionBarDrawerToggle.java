package com.gf.movie.reminder.util;


import android.app.Activity;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.ImageView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.application.MovieReminderApplication;

public class BlurActionBarDrawerToggle extends ActionBarDrawerToggle {

    private static final String TAG = BlurActionBarDrawerToggle.class.getSimpleName();

    private static final int DEFAULT_RADIUS = 10;
    private static final int DEFAULT_DOWN_SAMPLING = 3;

    private Activity mActivity;

    private View mContainer;
    private ImageView mBlurImage;
    private boolean mBlurEnabled;

    private int mBlurRadius;
    private int mDownSampling;

    /**
     * Construct a new ActionBarDrawerToggle.
     * <p/>
     * <p>The given {@link android.app.Activity} will be linked to the specified {@link android.support.v4.widget.DrawerLayout}.
     * The provided drawer indicator drawable will animate slightly off-screen as the drawer
     * is opened, indicating that in the open state the drawer will move off-screen when pressed
     * and in the closed state the drawer will move on-screen when pressed.</p>
     * <p/>
     * <p>String resources must be provided to describe the open/close drawer actions for
     * accessibility services.</p>
     *
     * @param activity                  The Activity hosting the drawer
     * @param drawerLayout              The DrawerLayout to link to the given Activity's ActionBar
     */
    public BlurActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout) {
        super(activity, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        mBlurEnabled = Utils.isDrawerLayoutBlurEnabled(PreferenceManager.getDefaultSharedPreferences(MovieReminderApplication.get(activity)));
        mActivity = activity;
        mBlurRadius = DEFAULT_RADIUS;
        mDownSampling = DEFAULT_DOWN_SAMPLING;

        mContainer = activity.findViewById(R.id.container);
        mBlurImage = (ImageView) activity.findViewById(R.id.blur_view);
    }

    public void setBlurEnabled(boolean blurEnabled) {
        mBlurEnabled = blurEnabled;
        if (blurEnabled) {
            setBlurImage();
        } else {
            clearBlurImage();
        }
    }

    public void setBlurRadius(int blurRadius) {
        if (0 < blurRadius && blurRadius <= 25) {
            mBlurRadius = blurRadius;
        }
    }

    public void setDownSampling(int downSampling) {
        mDownSampling = downSampling;
    }

    @Override
    public void onDrawerSlide(final View drawerView, final float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if (slideOffset > 0f) {
            setBlurAlpha(slideOffset);
        } else {
            clearBlurImage();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        if (mBlurEnabled) {
            clearBlurImage();
        }
    }

    private void setBlurAlpha(float slideOffset) {
        if (mBlurEnabled) {
            if (mBlurImage.getVisibility() != View.VISIBLE) {
                setBlurImage();
            }
            Utils.setAlpha(mBlurImage, slideOffset);
        }
    }

    public void setBlurImage() {
        mBlurImage.setImageBitmap(null);
        mBlurImage.setVisibility(View.VISIBLE);
        // do the downscaling for faster processing
        Bitmap downScaled = Utils.drawViewToBitmap(mContainer,
                mContainer.getWidth(), mContainer.getHeight(), mDownSampling);
        // apply the blur using the renderscript
        Bitmap blurred = Blur.apply(mActivity, downScaled, mBlurRadius);
        mBlurImage.setImageBitmap(blurred);
        downScaled.recycle();
    }

    public void clearBlurImage() {
        mBlurImage.setVisibility(View.GONE);
        mBlurImage.setImageBitmap(null);
    }
}