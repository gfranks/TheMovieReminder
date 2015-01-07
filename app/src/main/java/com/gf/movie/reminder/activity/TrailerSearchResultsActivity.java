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
import com.gf.movie.reminder.activity.base.BaseTrailerActivity;
import com.gf.movie.reminder.adapter.TrailersGridAdapter;
import com.gf.movie.reminder.data.api.RequestService;
import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.util.NotificationManager;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.FeedbackBar;
import com.gf.movie.reminder.view.pulltorefresh.PullToRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TrailerSearchResultsActivity extends BaseActivity implements Callback<ArrayList<Trailer>>,
        AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener, PullToRefreshLayout.OnRefreshListener,
        View.OnClickListener {

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

    private boolean mIsSelectingNewReminder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_search_results);

        mGrid = (GridView) findViewById(R.id.results_grid);
        mGrid.setOnItemClickListener(this);
        mGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGrid.setMultiChoiceModeListener(this);
        TextView emptyView = (TextView) findViewById(R.id.adapter_empty_text);
        emptyView.setText(getString(R.string.trailers_search_empty_text));
        mGrid.setEmptyView(emptyView);

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.results_pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(this);

        mExpandableFab.setVisibility(View.VISIBLE);
        mExpandableFab.animateVisibility(0f, 1f, View.VISIBLE);
        mExpandableFab.setFadeOnFabClick(false);
        mExpandableFab.setOnFabClickListener(this);

        onNewIntent(getIntent());
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

        if (trailers.size() > 0) {
            mExpandableFab.slideInFab();
        } else {
            mExpandableFab.slideOutFab();
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
            Intent intent;
            if (mAdapter.getItem(position) instanceof Movie) {
                intent = new Intent(this, MovieTrailerActivity.class);
            } else {
                intent = new Intent(this, GameTrailerActivity.class);
            }
            intent.putExtra(BaseTrailerActivity.EXTRA_TRAILER, mAdapter.getItem(position));
            if (Utils.isAtLeastLollipop() && Utils.isTransitionAnimationEnabled(mPrefs)) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.trailer_image_url), getString(R.string.trailer_transition_name));
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else {
            mIsSelectingNewReminder = false;
            if (mAdapter.getItem(position) instanceof Movie) {
                getFeedbackBar().showInfo((R.string.trailers_movie_reminder_set), false, FeedbackBar.LENGTH_LONG);
                mNotificationManager.registerNewMovieNotification(getApplicationContext(), (Movie) mAdapter.getItem(position));
            } else {
                getFeedbackBar().showInfo((R.string.trailers_game_reminder_set), false, FeedbackBar.LENGTH_LONG);
                mNotificationManager.registerNewGameNotification(getApplicationContext(), (Game) mAdapter.getItem(position));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mExpandableFab.isViewFabView(v)) {
            mIsSelectingNewReminder = !mIsSelectingNewReminder;
            if (mIsSelectingNewReminder) {
                getFeedbackBar().showInfo(R.string.trailers_select, false, FeedbackBar.LENGTH_LONG);
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
    public void onRefresh() {
        mRequestService.search(getString(R.string.google_api_key), mSearchQuery, this);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mSearchQuery = intent.getStringExtra(SearchManager.QUERY);
            onRefresh();
        }
    }
}
