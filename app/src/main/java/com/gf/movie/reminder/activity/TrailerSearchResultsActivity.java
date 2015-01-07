package com.gf.movie.reminder.activity;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.adapter.TrailersGridAdapter;
import com.gf.movie.reminder.data.api.RequestService;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.fragment.MovieTrailerBottomDragFragment;
import com.gf.movie.reminder.fragment.MovieTrailerTopDragFragment;
import com.gf.movie.reminder.fragment.base.BaseTrailerBottomDragFragment;
import com.gf.movie.reminder.fragment.base.BaseTrailerTopDragFragment;
import com.gf.movie.reminder.util.NotificationManager;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.FeedbackBar;
import com.gf.movie.reminder.view.pulltorefresh.PullToRefreshLayout;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggablePanel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TrailerSearchResultsActivity extends BaseActivity implements Callback<ArrayList<Trailer>>, AdapterView.OnItemClickListener,
        AbsListView.MultiChoiceModeListener, PullToRefreshLayout.OnRefreshListener, DraggableListener {

    @Inject
    RequestService mRequestService;

    @Inject
    Picasso mPicasso;

    @Inject
    NotificationManager mNotificationManager;

    @Inject
    SharedPreferences mPrefs;

    private String mSearchQuery;
    private GridView mGrid;
    private PullToRefreshLayout mPullToRefreshLayout;
    private TrailersGridAdapter mAdapter;
    private DraggablePanel mDraggablePanel;
    private ActionMode mActionMode;

    private boolean mIsSelectingNewReminder;

    private BaseTrailerTopDragFragment mTopFragment;
    private BaseTrailerBottomDragFragment mBottomFragment;
    private ActionMode.Callback mDraggableActionModeCallback = getDraggableActionModeCallback();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_search_results);
        handleIntent(getIntent());

        mGrid = (GridView) findViewById(R.id.results_grid);
        mGrid.setOnItemClickListener(this);
        mGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGrid.setMultiChoiceModeListener(this);
        TextView emptyView = (TextView) findViewById(R.id.adapter_empty_text);
        emptyView.setText(getString(R.string.trailers_search_empty_text));
        mGrid.setEmptyView(emptyView);

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.results_pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(this);

        mTopFragment = new MovieTrailerTopDragFragment();
        Bundle args = new Bundle();
        args.putBoolean(MovieTrailerTopDragFragment.EXTRA_IS_PANEL, true);
        mTopFragment.setArguments(args);
        mBottomFragment = new MovieTrailerBottomDragFragment();
        mDraggablePanel = (DraggablePanel) findViewById(R.id.results_draggable_panel);
        mDraggablePanel.setDraggableListener(this);
        mDraggablePanel.setFragmentManager(getSupportFragmentManager());
        mDraggablePanel.setTopFragment(mTopFragment);
        mDraggablePanel.setBottomFragment(mBottomFragment);
        mDraggablePanel.initializeView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public void success(ArrayList<Trailer> trailers, Response response) {
        mPullToRefreshLayout.setRefreshing(false);
        if (mAdapter == null) {
            mAdapter = new TrailersGridAdapter(this, trailers, mPicasso);
            mGrid.setAdapter(mAdapter);
        } else {
            mAdapter.setTrailers(trailers);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        mPullToRefreshLayout.setRefreshing(false);
        getFeedbackBar().showError(error.getMessage(), false, FeedbackBar.LENGTH_LONG);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!mIsSelectingNewReminder) {
            if (Utils.isTrailerPanelEnabled(mPrefs)) {
                setTitle(mAdapter.getItem(position).getTitle());
                mTopFragment.updateWithTrailer(mAdapter.getItem(position));
                mBottomFragment.updateWithTrailer(mAdapter.getItem(position));
                mDraggablePanel.setVisibility(View.VISIBLE);
                mDraggablePanel.maximize();
            } else {
                Intent intent = new Intent(this, MovieTrailerActivity.class);
                intent.putExtra(MovieTrailerActivity.EXTRA_TRAILER, mAdapter.getItem(position));
                if (Utils.isAtLeastLollipop() && Utils.isTransitionAnimationEnabled(mPrefs)) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.trailer_image_url), getString(R.string.trailer_transition_name));
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        } else {
            mIsSelectingNewReminder = false;
            getFeedbackBar().showInfo(R.string.trailers_movie_reminder_set, false, FeedbackBar.LENGTH_LONG);
            mNotificationManager.registerNewMovieNotification(getApplicationContext(), (Movie) mAdapter.getItem(position));
        }
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        mode.setTitle(String.format(getString(R.string.reminders_selected), mGrid.getCheckedItemCount()));
        mAdapter.toggleSelection(position);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.fragment_trailers_select, menu);
        getToolbar().setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        getToolbar().setVisibility(View.VISIBLE);
        mAdapter.removeSelection();
    }

    @Override
    public void onMaximized() {
        mActionMode = getToolbar().startActionMode(mDraggableActionModeCallback);
        getExpandableFab().slideOutFab();
        clearMenu();
    }

    @Override
    public void onMinimized() {
        mActionMode.finish();
        setTitle(getString(R.string.app_name));
        getExpandableFab().slideOutFab();
        initializeMenu();
    }

    @Override
    public void onClosedToLeft() {
        finishActionMode();
        setTitle(getString(R.string.app_name));
        getExpandableFab().slideInFab();
        initializeMenu();
        finishActionMode();
    }

    @Override
    public void onClosedToRight() {
        finishActionMode();
        setTitle(getString(R.string.app_name));
        getExpandableFab().slideInFab();
        initializeMenu();
    }

    @Override
    public void onRefresh() {
        mRequestService.search(mSearchQuery, this);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mSearchQuery = intent.getStringExtra(SearchManager.QUERY);
            onRefresh();
        }
    }

    protected void finishActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    private ActionMode.Callback getDraggableActionModeCallback() {
        return new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getToolbar().setVisibility(View.GONE);
                mTopFragment.onCreateOptionsMenu(menu, mode.getMenuInflater());
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle(mTopFragment.getTrailer().getTitle());
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (mTopFragment.onOptionsItemSelected(item)) {
                    mode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                getToolbar().setVisibility(View.VISIBLE);
                mDraggablePanel.minimize();
            }
        };
    }
}
