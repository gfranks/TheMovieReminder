package com.gf.movie.reminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminder;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.gf.movie.reminder.ui.Fab;
import com.gf.movie.reminder.util.MovieNotificationManager;
import com.gf.movie.reminder.view.FeedbackBar;

import javax.inject.Inject;

public class MovieTrailerBottomDragFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "movie_trailer_bottom";

    @Inject
    MovieNotificationManager mNotificationManager;

    private Movie mMovie;
    private MovieReminder mReminder;
    private TextView mMovieTitle;
    private TextView mMovieReleaseDate;
    private TextView mMovieDescription;
    private TextView mMovieDirectors;
    private TextView mMovieStars;
    private Fab mFab;

    private boolean mReminderSet;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_trailer_bottom_drag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMovieTitle = (TextView) view.findViewById(R.id.movie_title);
        mMovieReleaseDate = (TextView) view.findViewById(R.id.movie_release_date);
        mMovieDescription = (TextView) view.findViewById(R.id.movie_description);
        mMovieDirectors = (TextView) view.findViewById(R.id.movie_directors);
        mMovieStars = (TextView) view.findViewById(R.id.movie_stars);

        mFab = (Fab) view.findViewById(R.id.movie_fab);
        mFab.animateVisibility(0f, 1f, View.VISIBLE);
        mFab.setOnClickListener(this);
    }

    public void updateWithReminder(MovieReminder reminder) {
        mReminder = reminder;
        mMovie = reminder.getMovie();
        update();
    }

    public void updateWithMovie(Movie movie) {
        mReminder = null;
        mMovie = movie;
        update();
    }

    private void update() {
        mMovieTitle.setText(mMovie.getTitleString());
        mMovieReleaseDate.setText(mMovie.getReleaseDateString());
        mMovieDescription.setText(mMovie.getDescription());
        mMovieDirectors.setText(mMovie.getDirectors());
        mMovieStars.setText(mMovie.getStars());

        if (mReminder != null) {
            mFab.setImageResource(R.drawable.img_reminders_selected);
        } else {
            mFab.setImageResource(R.drawable.img_reminders_unselected);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mFab.getId()) {
            if (!mReminderSet) {
                mReminderSet = true;
                getFeedbackBar().showInfo(R.string.trailers_movie_reminder_set, false, FeedbackBar.LENGTH_LONG);
                if (mReminder != null) {
                    mNotificationManager.registerNewMovieNotification(getActivity().getApplicationContext(), mReminder);
                } else {
                    mNotificationManager.registerNewMovieNotification(getActivity().getApplicationContext(), mMovie);
                }
                mFab.setImageResource(R.drawable.img_reminders_selected);
            } else {
                if (mReminder != null) {
                    mReminderSet = false;
                    mNotificationManager.unregisterNewMovieNotification(getActivity().getApplicationContext(), mReminder);
                    getFeedbackBar().showInfo(R.string.trailers_movie_reminder_removed, false, FeedbackBar.LENGTH_LONG);
                    mFab.setImageResource(R.drawable.img_reminders_unselected);
                } else {
                    getFeedbackBar().showInfo(R.string.trailers_movie_reminder_already_set, false, FeedbackBar.LENGTH_LONG);
                }
            }
        }
    }
}
