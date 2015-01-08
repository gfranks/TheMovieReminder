package com.gf.movie.reminder.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.MenuItem;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.MovieTrailerActivity;
import com.gf.movie.reminder.adapter.TrailersGridAdapter;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.data.model.YoutubeMovieResponse;
import com.gf.movie.reminder.fragment.base.BaseTrailersFragment;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.FeedbackBar;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieTrailersFragment extends BaseTrailersFragment implements Callback<YoutubeMovieResponse> {

    public static final String TAG = "movie_trailers";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopFragment = new MovieTrailerTopDragFragment();
        Bundle args = new Bundle();
        args.putBoolean(MovieTrailerTopDragFragment.EXTRA_IS_PANEL, true);
        mTopFragment.setArguments(args);
        mBottomFragment = new MovieTrailerBottomDragFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.movie_trailers);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void success(YoutubeMovieResponse youtubeResponse, Response response) {
        mPullToRefreshLayout.setRefreshing(false);
        if (mAdapter == null) {
            mAdapter = new TrailersGridAdapter(getActivity(), youtubeResponse.getTrailers(), mPicasso);
            mGrid.setAdapter(mAdapter);
        } else {
            mAdapter.setTrailers(youtubeResponse.getTrailers());
        }
    }

    @Override
    public void failure(RetrofitError error) {
        mPullToRefreshLayout.setRefreshing(false);
        getFeedbackBar().showError(error.getMessage(), false, FeedbackBar.LENGTH_LONG);
    }

    @Override
    public void onFabClick() {
        super.onFabClick();
        if (mIsSelectingNewReminder) {
            getFeedbackBar().showInfo(R.string.trailers_select_movie, false, FeedbackBar.LENGTH_LONG);
        }
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reminder:
                final SparseBooleanArray selected = mAdapter.getSelectedIds().clone();
                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        mNotificationManager.registerNewMovieNotification(getActivity().getApplicationContext(), (Movie) mAdapter.getItem(selected.keyAt(i)));
                    }
                }
                getFeedbackBar().showInfo(selected.size() > 1 ? R.string.trailers_movie_reminders_set : R.string.trailers_movie_reminder_set, false, FeedbackBar.LENGTH_LONG);
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRefresh() {
        mRequestService.getMovieTrailers(getString(R.string.google_api_key), this);
    }

    @Override
    protected void startTrailerActivity(Trailer trailer) {
        Intent intent = new Intent(getActivity(), MovieTrailerActivity.class);
        intent.putExtra(MovieTrailerActivity.EXTRA_TRAILER, trailer);
        if (Utils.isAtLeastLollipop() && Utils.isTransitionAnimationEnabled(mPrefs)) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), getView().findViewById(R.id.trailer_image_url), getString(R.string.trailer_transition_name));
            getActivity().startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    protected void didSelectTrailerForReminder(Trailer trailer) {
        super.didSelectTrailerForReminder(trailer);
        getFeedbackBar().showInfo(R.string.trailers_movie_reminder_set, false, FeedbackBar.LENGTH_LONG);
        mNotificationManager.registerNewMovieNotification(getActivity().getApplicationContext(), (Movie) trailer);
    }

    @Override
    protected void setFragmentTitle() {
        getActivity().setTitle(R.string.movie_trailers);
    }
}
