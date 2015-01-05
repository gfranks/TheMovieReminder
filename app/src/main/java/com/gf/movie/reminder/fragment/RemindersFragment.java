package com.gf.movie.reminder.fragment;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.MovieTrailerActivity;
import com.gf.movie.reminder.adapter.RemindersGridAdapter;
import com.gf.movie.reminder.data.model.MovieReminder;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.gf.movie.reminder.util.MovieReminderManager;
import com.gf.movie.reminder.util.Utils;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggablePanel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

public class RemindersFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        AbsListView.MultiChoiceModeListener, DraggableListener {

    public static final String TAG = "reminders";

    @Inject
    Picasso mPicasso;

    @Inject
    MovieReminderManager mReminderManager;

    @Inject
    SharedPreferences mPrefs;

    private GridView mGrid;
    private RemindersGridAdapter mAdapter;
    private DraggablePanel mDraggablePanel;

    private MovieTrailerTopDragFragment mMovieTrailerTopDragFragment;
    private MovieTrailerBottomDragFragment mMovieTrailerBottomDragFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGrid = (GridView) view.findViewById(R.id.reminders_grid);
        mGrid.setOnItemClickListener(this);
        mGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGrid.setMultiChoiceModeListener(this);
        TextView emptyView = (TextView) view.findViewById(R.id.adapter_empty_text);
        emptyView.setText(getString(R.string.reminders_empty_text));
        mGrid.setEmptyView(emptyView);

        mMovieTrailerTopDragFragment = new MovieTrailerTopDragFragment();
        mMovieTrailerBottomDragFragment = new MovieTrailerBottomDragFragment();
        mDraggablePanel = (DraggablePanel) view.findViewById(R.id.reminders_draggable_panel);
        mDraggablePanel.setDraggableListener(this);
        mDraggablePanel.setFragmentManager(getChildFragmentManager());
        mDraggablePanel.setTopFragment(mMovieTrailerTopDragFragment);
        mDraggablePanel.setBottomFragment(mMovieTrailerBottomDragFragment);
        mDraggablePanel.initializeView();
        mDraggablePanel.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.reminders);

        getReminders();
    }

    private void getReminders() {
        ArrayList<MovieReminder> reminders = mReminderManager.getAll();
        if (mAdapter == null) {
            mAdapter = new RemindersGridAdapter(getActivity(), reminders, mPicasso);
            mGrid.setAdapter(mAdapter);
        } else {
            mAdapter.setReminders(reminders);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (Utils.isTrailerPanelEnabled(mPrefs)) {
            getActivity().setTitle(mAdapter.getItem(position).getMovie().getTitle());
            mMovieTrailerTopDragFragment.updateWithReminder(mAdapter.getItem(position));
            mMovieTrailerBottomDragFragment.updateWithReminder(mAdapter.getItem(position));
            mDraggablePanel.setVisibility(View.VISIBLE);
            mDraggablePanel.maximize();
        } else {
            Intent intent = new Intent(getActivity(), MovieTrailerActivity.class);
            intent.putExtra(MovieTrailerActivity.EXTRA_MOVIE_REMINDER, mAdapter.getItem(position));

            if (Utils.isAtLeastLollipop() && Utils.isTransitionAnimationEnabled(mPrefs)) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.movie_image_url), "movie_image");
                getActivity().startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        mode.setTitle(String.format(getString(R.string.reminders_selected), mGrid.getCheckedItemCount()));
        mAdapter.toggleSelection(position);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.fragment_reminders, menu);
        getToolbar().setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
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
                                        MovieReminder reminder = mAdapter.getItem(selected.keyAt(i));
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
    public void onDestroyActionMode(ActionMode mode) {
        getToolbar().setVisibility(View.VISIBLE);
        mAdapter.removeSelection();
    }

    @Override
    public void onMaximized() {
    }

    @Override
    public void onMinimized() {
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onClosedToLeft() {
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onClosedToRight() {
        getActivity().setTitle(getString(R.string.app_name));
    }
}
