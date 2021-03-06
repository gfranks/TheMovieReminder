package com.gf.movie.reminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.GameReminder;
import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.fragment.base.BaseTrailerBottomDragFragment;
import com.gf.movie.reminder.ui.Fab;
import com.gf.movie.reminder.util.NotificationManager;
import com.gf.movie.reminder.view.FeedbackBar;

import javax.inject.Inject;

public class GameTrailerBottomDragFragment extends BaseTrailerBottomDragFragment implements View.OnClickListener {

    public static final String TAG = "game_trailer_bottom";

    @Inject
    NotificationManager mNotificationManager;

    private TextView mGameTitle;
    private TextView mGameReleaseDate;
    private TextView mGameDescription;
    private TextView mGameCompany;
    private Fab mFab;

    private boolean mReminderSet;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_trailer_bottom_drag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGameTitle = (TextView) view.findViewById(R.id.game_title);
        mGameReleaseDate = (TextView) view.findViewById(R.id.game_release_date);
        mGameDescription = (TextView) view.findViewById(R.id.game_description);
        mGameCompany = (TextView) view.findViewById(R.id.game_company);

        mFab = (Fab) view.findViewById(R.id.game_fab);
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
                getFeedbackBar().showInfo(R.string.trailers_game_reminder_set, false, FeedbackBar.LENGTH_LONG);
                if (mReminder != null) {
                    mNotificationManager.registerNewGameReminderNotification(getActivity().getApplicationContext(), (GameReminder) mReminder);
                } else {
                    mNotificationManager.registerNewGameNotification(getActivity().getApplicationContext(), (Game) mTrailer);
                }
                mFab.setImageResource(R.drawable.img_reminders_selected);
            } else {
                if (mReminder != null) {
                    mReminderSet = false;
                    mNotificationManager.unregisterNewGameReminderNotification(getActivity().getApplicationContext(), (GameReminder) mReminder);
                    getFeedbackBar().showInfo(R.string.trailers_game_reminder_removed, false, FeedbackBar.LENGTH_LONG);
                    mFab.setImageResource(R.drawable.img_reminders_unselected);
                } else {
                    getFeedbackBar().showInfo(R.string.trailers_game_reminder_already_set, false, FeedbackBar.LENGTH_LONG);
                }
            }
        }
    }

    private void update() {
        mGameTitle.setText(mTrailer.getTitleString());
        mGameReleaseDate.setText(mTrailer.getReleaseDateString());
        mGameDescription.setText(mTrailer.getDescription());
        mGameCompany.setText(((Game) mTrailer).getCompany());

        if (mReminder != null) {
            mFab.setImageResource(R.drawable.img_reminders_selected);
        } else {
            mFab.setImageResource(R.drawable.img_reminders_unselected);
        }
    }
}
