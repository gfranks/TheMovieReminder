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
import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.fragment.base.BaseTrailerBottomDragFragment;
import com.gf.movie.reminder.ui.Fab;
import com.gf.movie.reminder.util.NotificationManager;
import com.gf.movie.reminder.view.FeedbackBar;

import javax.inject.Inject;

public class MovieTrailerBottomDragFragment extends BaseTrailerBottomDragFragment implements View.OnClickListener {

    public static final String TAG = "movie_trailer_bottom";

    @Inject
    NotificationManager mNotificationManager;

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

    @Override
    public void updateWithReminder(Reminder reminder) {
        super.updateWithReminder(reminder);
        update();
    }

    @Override
    public void updateWithTrailer(Trailer trailer) {
        super.updateWithTrailer(trailer);
        update();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mFab.getId()) {
            if (!mReminderSet) {
                mReminderSet = true;
                getFeedbackBar().showInfo(R.string.trailers_movie_reminder_set, false, FeedbackBar.LENGTH_LONG);
                if (mReminder != null) {
                    mNotificationManager.registerNewMovieReminderNotification(getActivity().getApplicationContext(), (MovieReminder) mReminder);
                } else {
                    mNotificationManager.registerNewMovieNotification(getActivity().getApplicationContext(), (Movie) mTrailer);
                }
                mFab.setImageResource(R.drawable.img_reminders_selected);
            } else {
                if (mReminder != null) {
                    mReminderSet = false;
                    mNotificationManager.unregisterNewMovieReminderNotification(getActivity().getApplicationContext(), (MovieReminder) mReminder);
                    getFeedbackBar().showInfo(R.string.trailers_movie_reminder_removed, false, FeedbackBar.LENGTH_LONG);
                    mFab.setImageResource(R.drawable.img_reminders_unselected);
                } else {
                    getFeedbackBar().showInfo(R.string.trailers_movie_reminder_already_set, false, FeedbackBar.LENGTH_LONG);
                }
            }
        }
    }

    private void update() {
        mMovieTitle.setText(mTrailer.getTitleString());
        mMovieReleaseDate.setText(mTrailer.getReleaseDateString());
        mMovieDescription.setText(mTrailer.getDescription());
        mMovieDirectors.setText(((Movie) mTrailer).getDirectors());
        mMovieStars.setText(((Movie) mTrailer).getStars());

        if (mReminder != null) {
            mFab.setImageResource(R.drawable.img_reminders_selected);
        } else {
            mFab.setImageResource(R.drawable.img_reminders_unselected);
        }
    }
}
