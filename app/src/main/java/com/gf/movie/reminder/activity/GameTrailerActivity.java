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
import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.GameReminder;
import com.gf.movie.reminder.ui.Fab;
import com.gf.movie.reminder.util.NotificationManager;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.FeedbackBar;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class GameTrailerActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_GAME = "game";
    public static final String EXTRA_GAME_REMINDER = "reminder";
    public static final String EXTRA_NOTIFICATION = "notification";

    @Inject
    Picasso mPicasso;

    @Inject
    NotificationManager mNotificationManager;

    @Inject
    SharedPreferences mPrefs;

    private Game mGame;
    private GameReminder mReminder;
    private View mGamePlay;
    private ImageView mGameImage;
    private ImageView mGameReminderSet;
    private TextView mGameTitle;
    private TextView mGameReleaseDate;
    private TextView mGameDescription;
    private TextView mGameConsole;
    private TextView mGameCompany;
    private Fab mFab;

    private boolean mReminderSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_trailer);

        mGame = getIntent().getExtras().getParcelable(EXTRA_GAME);
        mReminder = getIntent().getExtras().getParcelable(EXTRA_GAME_REMINDER);

        if (mReminder != null) {
            mReminderSet = true;
            mGame = (Game) mReminder.getTrailer();
            if (getIntent().hasExtra(EXTRA_NOTIFICATION)) {
                getFeedbackBar().showInfo(String.format(getString(R.string.trailers_game_notified), mGame.getTitle()), false, FeedbackBar.LENGTH_LONG);
            }
        }

        mGamePlay = findViewById(R.id.game_play);
        mGameImage = (ImageView) findViewById(R.id.game_image_url);
        mGameReminderSet = (ImageView) findViewById(R.id.game_reminder_set);
        mGameTitle = (TextView) findViewById(R.id.game_title);
        mGameReleaseDate = (TextView) findViewById(R.id.game_release_date);
        mGameDescription = (TextView) findViewById(R.id.game_description);
        mGameConsole = (TextView) findViewById(R.id.game_console);
        mGameCompany = (TextView) findViewById(R.id.game_company);

        mGamePlay.setOnClickListener(this);

        mFab = (Fab) findViewById(R.id.game_fab);
        mFab.animateVisibility(0f, 1f, View.VISIBLE);
        mFab.setOnClickListener(this);

        TextView releasedTV = (TextView) findViewById(R.id.trailer_released);
        if (mGame.isReleased()) {
            releasedTV.setVisibility(View.VISIBLE);
            releasedTV.setText(getString(R.string.trailers_game_released));
        } else {
            releasedTV.setVisibility(View.GONE);
        }

        setTitle(mGame.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPicasso.load(mGame.getImageUrl())
//                .centerCrop()
                .placeholder(R.drawable.img_photo_loading_small)
                .error(R.drawable.img_failed_to_receive_small)
                .into(mGameImage);
        mGameTitle.setText(mGame.getTitleString());
        mGameReleaseDate.setText(mGame.getReleaseDateString());
        mGameDescription.setText(mGame.getDescription());
        mGameConsole.setText(mGame.getConsoleName());
        mGameCompany.setText(mGame.getCompany());

        if (mReminder != null) {
            mFab.setImageResource(R.drawable.img_reminders_selected);
            mGameReminderSet.setVisibility(View.VISIBLE);
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
        if (v.getId() == mGamePlay.getId()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mGame.getVideoUrl())));
        } else if (v.getId() == mFab.getId()) {
            if (!mReminderSet) {
                mReminderSet = true;
                getFeedbackBar().showInfo(R.string.trailers_game_reminder_set, false, FeedbackBar.LENGTH_LONG);
                if (mReminder != null) {
                    mNotificationManager.registerNewGameNotification(getApplicationContext(), mReminder);
                } else {
                    mNotificationManager.registerNewGameNotification(getApplicationContext(), mGame);
                }
                mGameReminderSet.setVisibility(View.VISIBLE);
                mFab.setImageResource(R.drawable.img_reminders_selected);
            } else {
                if (mReminder != null) {
                    mReminderSet = false;
                    mGameReminderSet.setVisibility(View.INVISIBLE);
                    mNotificationManager.unregisterNewGameNotification(getApplicationContext(), mReminder);
                    getFeedbackBar().showInfo(R.string.trailers_game_reminder_removed, false, FeedbackBar.LENGTH_LONG);
                    mFab.setImageResource(R.drawable.img_reminders_unselected);
                } else {
                    getFeedbackBar().showInfo(R.string.trailers_game_reminder_already_set, false, FeedbackBar.LENGTH_LONG);
                }
            }
        }
    }
}
