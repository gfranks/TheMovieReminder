package com.gf.movie.reminder.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gf.movie.reminder.BuildConfig;
import com.gf.movie.reminder.R;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.util.Utils;

public class FeedbackBar extends TextView {

    public static final long LENGTH_SHORT = 1500;
    public static final long LENGTH_LONG = 3000;

    private static final int ERROR = R.drawable.bg_feedback_red;
    private static final int INFO = R.drawable.bg_feedback_blue;
    private static final int WARNING = R.drawable.bg_feedback_yellow;

    private Handler mHandler = null;
    private Runnable mRunnable = null;
    private boolean mIsSticky = false;

    public FeedbackBar(Context context) {
        super(context);
        init();
    }

    public FeedbackBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FeedbackBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setVisibility(GONE);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                doHide();
            }
        };

        if (Utils.isAtLeastLollipop()) {
//            setElevation(getResources().getDimensionPixelSize(R.dimen.shadow_elevation));
        }
    }

    public void showError(int errorText, boolean isSticky) {
        showError(getContext().getString(errorText), isSticky, LENGTH_SHORT);
    }

    public void showError(int errorText, boolean isSticky, long duration) {
        showError(getContext().getString(errorText), isSticky, duration);
    }

    public void showError(String errorText, boolean isSticky) {
        showError(errorText, isSticky, LENGTH_SHORT);
    }

    public void showError(String errorText, boolean isSticky, long duration) {
        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "Showing feedbackBar error");
        if (isSticky && !mIsSticky && getVisibility() == VISIBLE)
            return; //Sticky have lower priority
        showMessage(ERROR, errorText, isSticky, duration);
    }

    public void showInfo(int infoText, boolean isSticky) {
        showInfo(getContext().getString(infoText), isSticky, LENGTH_SHORT);
    }

    public void showInfo(int infoText, boolean isSticky, long duration) {
        showInfo(getContext().getString(infoText), isSticky, duration);
    }

    public void showInfo(String infoText, boolean isSticky) {
        showInfo(infoText, isSticky, LENGTH_SHORT);
    }

    public void showInfo(String infoText, boolean isSticky, long duration) {
        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "Showing feedbackBar info");
        //doHide();
        showMessage(INFO, infoText, isSticky, duration);
    }

    public void showWarning(int infoText, boolean isSticky) {
        showWarning(getContext().getString(infoText), isSticky, LENGTH_SHORT);
    }

    public void showWarning(int infoText, boolean isSticky, long duration) {
        showWarning(getContext().getString(infoText), isSticky, duration);
    }

    public void showWarning(String infoText, boolean isSticky) {
        showWarning(infoText, isSticky, LENGTH_SHORT);
    }

    public void showWarning(String infoText, boolean isSticky, long duration) {
        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "Showing feedbackBar info");
        //doHide();
        showMessage(WARNING, infoText, isSticky, duration);
    }

    public void clearSticky() {
        if (mIsSticky)
            doHide();
    }

    private void showMessage(int type, String text, boolean sticky, long duration) {
        if (!isEnabled())
            return;
        mHandler.removeCallbacks(mRunnable);
        setBackgroundResource(type);
        setText(text);
        if (type == WARNING) {
            setTextColor(getResources().getColor(R.color.theme_gray_dark));
        } else {
            setTextColor(getResources().getColor(R.color.theme_white));
        }
        mIsSticky = sticky;
        doShow();
        if (!sticky)
            mHandler.postDelayed(mRunnable, duration);
    }

    private void doShow() {
        clearAnimation();
        if (getVisibility() == VISIBLE)
            return;
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
        startAnimation(anim);
        setVisibility(VISIBLE);
    }

    private void doHide() {
        clearAnimation();
        mHandler.removeCallbacks(mRunnable);
        if (getVisibility() == GONE)
            return;
        mIsSticky = false;
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_top);
        startAnimation(anim);
        setVisibility(GONE);
    }

    public interface PendingFeedbackMessage {
        void showPendingFeedbackMessage(FeedbackBar helper);
    }
}
