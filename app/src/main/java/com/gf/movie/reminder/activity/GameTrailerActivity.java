package com.gf.movie.reminder.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.GameReminder;
import com.gf.movie.reminder.fragment.GameTrailerBottomDragFragment;
import com.gf.movie.reminder.fragment.GameTrailerTopDragFragment;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.FeedbackBar;

import javax.inject.Inject;

public class GameTrailerActivity extends BaseActivity {

    public static final String EXTRA_GAME = "game";
    public static final String EXTRA_GAME_REMINDER = "reminder";
    public static final String EXTRA_NOTIFICATION = "notification";

    @Inject
    SharedPreferences mPrefs;

    private Game mGame;
    private GameReminder mReminder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_trailer);

        mGame = getIntent().getExtras().getParcelable(EXTRA_GAME);
        mReminder = getIntent().getExtras().getParcelable(EXTRA_GAME_REMINDER);

        if (mReminder != null) {
            mGame = (Game) mReminder.getTrailer();
            if (getIntent().hasExtra(EXTRA_NOTIFICATION)) {
                getFeedbackBar().showInfo(String.format(getString(R.string.trailers_game_notified), mGame.getTitle()), false, FeedbackBar.LENGTH_LONG);
            }
        }


        setTitle(mGame.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupGame();
    }

    @Override
    public void finish() {
        if (Utils.isAtLeastLollipop() && Utils.isTransitionAnimationEnabled(mPrefs)) {
            finishAfterTransition();
        } else {
            super.finish();
        }
    }

    private void setupGame() {
        GameTrailerTopDragFragment topFrag = (GameTrailerTopDragFragment) getSupportFragmentManager().findFragmentById(R.id.game_top_fragment);
        GameTrailerBottomDragFragment bottomFrag = (GameTrailerBottomDragFragment) getSupportFragmentManager().findFragmentById(R.id.game_bottom_fragment);

        if (mReminder != null) {
            topFrag.updateWithReminder(mReminder);
            bottomFrag.updateWithReminder(mReminder);
        } else {
            topFrag.updateWithGame(mGame);
            bottomFrag.updateWithGame(mGame);
        }
    }
}
