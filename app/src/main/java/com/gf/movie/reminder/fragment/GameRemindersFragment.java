package com.gf.movie.reminder.fragment;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.GameTrailerActivity;
import com.gf.movie.reminder.adapter.RemindersGridAdapter;
import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.fragment.base.BaseRemindersFragment;
import com.gf.movie.reminder.util.GameReminderManager;
import com.gf.movie.reminder.util.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

public class GameRemindersFragment extends BaseRemindersFragment {

    public static final String TAG = "game_reminders";

    @Inject
    GameReminderManager mReminderManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopFragment = new GameTrailerTopDragFragment();
        mBottomFragment = new GameTrailerBottomDragFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_reminders, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.game_reminders);
    }

    @Override
    protected void getReminders() {
        ArrayList<Reminder> reminders = mReminderManager.getAll();
        if (mAdapter == null) {
            mAdapter = new RemindersGridAdapter(getActivity(), reminders, mPicasso);
            mGrid.setAdapter(mAdapter);
        } else {
            mAdapter.setReminders(reminders);
        }
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                final SparseBooleanArray selected = mAdapter.getSelectedIds().clone();
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.reminders_delete_title)
                        .setMessage(R.string.reminders_delete_message)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = (selected.size() - 1); i >= 0; i--) {
                                    if (selected.valueAt(i)) {
                                        Reminder reminder = mAdapter.getItem(selected.keyAt(i));
                                        mReminderManager.deleteReminder(reminder);
                                        mAdapter.remove(reminder);
                                    }
                                }
                                mode.finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mode.finish();
                            }
                        })
                        .show();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void startRemindersActivity(Reminder reminder) {
        Intent intent = new Intent(getActivity(), GameTrailerActivity.class);
        intent.putExtra(GameTrailerActivity.EXTRA_TRAILER_REMINDER, reminder);

        if (Utils.isAtLeastLollipop() && Utils.isTransitionAnimationEnabled(mPrefs)) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), getView().findViewById(R.id.trailer_image_url), getString(R.string.trailer_transition_name));
            getActivity().startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}
