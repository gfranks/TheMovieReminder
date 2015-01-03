package com.gf.movie.reminder.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminder;
import com.gf.movie.reminder.fragment.MovieTrailerBottomDragFragment;
import com.gf.movie.reminder.fragment.MovieTrailerTopDragFragment;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.FeedbackBar;

import javax.inject.Inject;

public class MovieTrailerActivity extends BaseActivity {

    public static final String EXTRA_MOVIE = "game";
    public static final String EXTRA_MOVIE_REMINDER = "reminder";
    public static final String EXTRA_NOTIFICATION = "notification";

    @Inject
    SharedPreferences mPrefs;

    private Movie mMovie;
    private MovieReminder mReminder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        mMovie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
        mReminder = getIntent().getExtras().getParcelable(EXTRA_MOVIE_REMINDER);

        if (mReminder != null) {
            mMovie = (Movie) mReminder.getTrailer();
            if (getIntent().hasExtra(EXTRA_NOTIFICATION)) {
                getFeedbackBar().showInfo(String.format(getString(R.string.trailers_movie_notified), mMovie.getTitle()), false, FeedbackBar.LENGTH_LONG);
            }
        }


        setTitle(mMovie.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupMovie();
    }

    @Override
    public void finish() {
        if (Utils.isAtLeastLollipop() && Utils.isTransitionAnimationEnabled(mPrefs)) {
            finishAfterTransition();
        } else {
            super.finish();
        }
    }

    private void setupMovie() {
        MovieTrailerTopDragFragment topFrag = (MovieTrailerTopDragFragment) getSupportFragmentManager().findFragmentById(R.id.movie_top_fragment);
        MovieTrailerBottomDragFragment bottomFrag = (MovieTrailerBottomDragFragment) getSupportFragmentManager().findFragmentById(R.id.movie_bottom_fragment);

        if (mReminder != null) {
            topFrag.updateWithReminder(mReminder);
            bottomFrag.updateWithReminder(mReminder);
        } else {
            topFrag.updateWithMovie(mMovie);
            bottomFrag.updateWithMovie(mMovie);
        }
    }
}
