package com.gf.movie.reminder.activity.base;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.fragment.base.BaseTrailerBottomDragFragment;
import com.gf.movie.reminder.fragment.base.BaseTrailerTopDragFragment;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.FeedbackBar;

import javax.inject.Inject;

public class BaseTrailerActivity extends BaseActivity {

    public static final String EXTRA_TRAILER = "trailer";
    public static final String EXTRA_TRAILER_REMINDER = "reminder";
    public static final String EXTRA_NOTIFICATION = "notification";

    @Inject
    protected SharedPreferences mPrefs;

    protected Trailer mTrailer;
    protected Reminder mReminder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTrailer = getIntent().getExtras().getParcelable(EXTRA_TRAILER);
        mReminder = getIntent().getExtras().getParcelable(EXTRA_TRAILER_REMINDER);

        if (mReminder != null) {
            mTrailer = mReminder.getTrailer();
            if (getIntent().hasExtra(EXTRA_NOTIFICATION)) {
                getFeedbackBar().showInfo(String.format(getString(R.string.trailers_movie_notified), mTrailer.getTitle()), false, FeedbackBar.LENGTH_LONG);
            }
        }


        setTitle(mTrailer.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupTrailer();
    }

    @Override
    public void finish() {
        if (Utils.isAtLeastLollipop() && Utils.isTransitionAnimationEnabled(mPrefs)) {
            finishAfterTransition();
        } else {
            super.finish();
        }
    }

    private void setupTrailer() {
        BaseTrailerTopDragFragment topFrag = (BaseTrailerTopDragFragment) getSupportFragmentManager().findFragmentById(R.id.trailer_top_fragment);
        BaseTrailerBottomDragFragment bottomFrag = (BaseTrailerBottomDragFragment) getSupportFragmentManager().findFragmentById(R.id.trailer_bottom_fragment);

        if (mReminder != null) {
            topFrag.updateWithReminder(mReminder);
            bottomFrag.updateWithReminder(mReminder);
        } else {
            topFrag.updateWithTrailer(mTrailer);
            bottomFrag.updateWithTrailer(mTrailer);
        }
    }
}
