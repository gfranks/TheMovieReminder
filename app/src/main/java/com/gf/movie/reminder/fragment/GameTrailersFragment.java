package com.gf.movie.reminder.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.GameTrailerActivity;
import com.gf.movie.reminder.adapter.TrailersGridAdapter;
import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.fragment.base.BaseTrailersFragment;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.FeedbackBar;
import com.gf.movie.reminder.view.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameTrailersFragment extends BaseTrailersFragment implements Callback<ArrayList<Game>>,
        AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener, PullToRefreshLayout.OnRefreshListener {

    public static final String TAG = "game_trailers";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopFragment = new GameTrailerTopDragFragment();
        mBottomFragment = new GameTrailerBottomDragFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_trailers, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.game_trailers);

        mRequestService.getGameTrailers(getString(R.string.google_api_key), this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void success(ArrayList<Game> games, Response response) {
        mPullToRefreshLayout.setRefreshing(false);
        if (mAdapter == null) {
            mAdapter = new TrailersGridAdapter(getActivity(), games, mPicasso);
            mGrid.setAdapter(mAdapter);
        } else {
            mAdapter.setTrailers(games);
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
            getFeedbackBar().showInfo(R.string.trailers_select_game, false, FeedbackBar.LENGTH_LONG);
        }
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reminder:
                final SparseBooleanArray selected = mAdapter.getSelectedIds().clone();
                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        mNotificationManager.registerNewGameNotification(getActivity().getApplicationContext(), (Game) mAdapter.getItem(selected.keyAt(i)));
                    }
                }
                getFeedbackBar().showInfo(selected.size() > 1 ? R.string.trailers_game_reminders_set : R.string.trailers_game_reminder_set, false, FeedbackBar.LENGTH_LONG);
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRefresh() {
        mRequestService.getGameTrailers(getString(R.string.google_api_key), this);
    }

    @Override
    protected void startTrailerActivity(Trailer trailer) {
        Intent intent = new Intent(getActivity(), GameTrailerActivity.class);
        intent.putExtra(GameTrailerActivity.EXTRA_TRAILER, trailer);
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
        getFeedbackBar().showInfo(R.string.trailers_game_reminder_set, false, FeedbackBar.LENGTH_LONG);
        mNotificationManager.registerNewGameNotification(getActivity().getApplicationContext(), (Game) trailer);
    }
}
