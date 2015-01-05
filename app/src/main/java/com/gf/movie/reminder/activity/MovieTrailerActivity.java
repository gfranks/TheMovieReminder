package com.gf.movie.reminder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminder;
import com.gf.movie.reminder.ui.Fab;
import com.gf.movie.reminder.util.NotificationManager;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.FeedbackBar;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class MovieTrailerActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_MOVIE = "movie";
    public static final String EXTRA_MOVIE_REMINDER = "reminder";
    public static final String EXTRA_NOTIFICATION = "notification";

    @Inject
    Picasso mPicasso;

    @Inject
    NotificationManager mNotificationManager;

    @Inject
    SharedPreferences mPrefs;

    private Movie mMovie;
    private MovieReminder mReminder;
    private View mMoviePlay;
    private ImageView mMovieImage;
    private ImageView mMovieReminderSet;
    private TextView mMovieTitle;
    private TextView mMovieReleaseDate;
    private TextView mMovieDescription;
    private TextView mMovieDirectors;
    private TextView mMovieStars;
    private Fab mFab;

    private boolean mReminderSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        mMovie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
        mReminder = getIntent().getExtras().getParcelable(EXTRA_MOVIE_REMINDER);

        if (mReminder != null) {
            mReminderSet = true;
            mMovie = (Movie) mReminder.getTrailer();
            if (getIntent().hasExtra(EXTRA_NOTIFICATION)) {
                getFeedbackBar().showInfo(String.format(getString(R.string.trailers_movie_notified), mMovie.getTitle()), false, FeedbackBar.LENGTH_LONG);
            }
        }

        mMoviePlay = findViewById(R.id.movie_play);
        mMovieImage = (ImageView) findViewById(R.id.movie_image_url);
        mMovieReminderSet = (ImageView) findViewById(R.id.movie_reminder_set);
        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMovieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        mMovieDescription = (TextView) findViewById(R.id.movie_description);
        mMovieDirectors = (TextView) findViewById(R.id.movie_directors);
        mMovieStars = (TextView) findViewById(R.id.movie_stars);

        mMoviePlay.setOnClickListener(this);

        mFab = (Fab) findViewById(R.id.movie_fab);
        mFab.animateVisibility(0f, 1f, View.VISIBLE);
        mFab.setOnClickListener(this);

        TextView releasedTV = (TextView) findViewById(R.id.trailer_released);
        if (mMovie.isReleased()) {
            releasedTV.setVisibility(View.VISIBLE);
            releasedTV.setText(getString(R.string.trailers_movie_in_theaters));
        } else {
            releasedTV.setVisibility(View.GONE);
        }

        setTitle(mMovie.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPicasso.load(mMovie.getImageUrl())
//                .centerCrop()
                .placeholder(R.drawable.img_photo_loading_small)
                .error(R.drawable.img_failed_to_receive_small)
                .into(mMovieImage);
        mMovieTitle.setText(mMovie.getTitleString());
        mMovieReleaseDate.setText(mMovie.getReleaseDateString());
        mMovieDescription.setText(mMovie.getDescription());
        mMovieDirectors.setText(mMovie.getDirectors());
        mMovieStars.setText(mMovie.getStars());

        if (mReminder != null) {
            mFab.setImageResource(R.drawable.img_reminders_selected);
            mMovieReminderSet.setVisibility(View.VISIBLE);
        } else {
            mFab.setImageResource(R.drawable.img_reminders_unselected);
        }
    }

    @Override
    public void finish() {
        if (Utils.isAtLeastLollipop() && Utils.isTransitionAnimationEnabled(mPrefs)) {
            finishAfterTransition();
        } else {
            super.finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mMoviePlay.getId()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mMovie.getVideoUrl())));
        } else if (v.getId() == mFab.getId()) {
            if (!mReminderSet) {
                mReminderSet = true;
                getFeedbackBar().showInfo(R.string.trailers_movie_reminder_set, false, FeedbackBar.LENGTH_LONG);
                if (mReminder != null) {
                    mNotificationManager.registerNewMovieNotification(getApplicationContext(), mReminder);
                } else {
                    mNotificationManager.registerNewMovieNotification(getApplicationContext(), mMovie);
                }
                mMovieReminderSet.setVisibility(View.VISIBLE);
                mFab.setImageResource(R.drawable.img_reminders_selected);
            } else {
                if (mReminder != null) {
                    mReminderSet = false;
                    mMovieReminderSet.setVisibility(View.INVISIBLE);
                    mNotificationManager.unregisterNewMovieNotification(getApplicationContext(), mReminder);
                    getFeedbackBar().showInfo(R.string.trailers_movie_reminder_removed, false, FeedbackBar.LENGTH_LONG);
                    mFab.setImageResource(R.drawable.img_reminders_unselected);
                } else {
                    getFeedbackBar().showInfo(R.string.trailers_movie_reminder_already_set, false, FeedbackBar.LENGTH_LONG);
                }
            }
        }
    }
}
