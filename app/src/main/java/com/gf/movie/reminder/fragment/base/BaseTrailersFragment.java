package com.gf.movie.reminder.fragment.base;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.TrailerSearchResultsActivity;
import com.gf.movie.reminder.adapter.TrailersGridAdapter;
import com.gf.movie.reminder.data.api.RequestService;
import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.util.NotificationManager;
import com.gf.movie.reminder.util.Utils;
import com.gf.movie.reminder.view.pulltorefresh.PullToRefreshLayout;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggablePanel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public abstract class BaseTrailersFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        AbsListView.MultiChoiceModeListener, PullToRefreshLayout.OnRefreshListener, DraggableListener {

    @Inject
    protected RequestService mRequestService;

    @Inject
    protected Picasso mPicasso;

    @Inject
    protected NotificationManager mNotificationManager;

    @Inject
    protected SharedPreferences mPrefs;

    protected GridView mGrid;
    protected PullToRefreshLayout mPullToRefreshLayout;
    protected TrailersGridAdapter mAdapter;
    protected DraggablePanel mDraggablePanel;
    protected ActionMode mActionMode;

    protected boolean mIsSelectingNewReminder;

    protected BaseTrailerTopDragFragment mTopFragment;
    protected BaseTrailerBottomDragFragment mBottomFragment;
    protected ActionMode.Callback mDraggableActionModeCallback = getDraggableActionModeCallback();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trailers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGrid = (GridView) view.findViewById(R.id.trailers_grid);
        mGrid.setOnItemClickListener(this);
        mGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGrid.setMultiChoiceModeListener(this);
        TextView emptyView = (TextView) view.findViewById(R.id.adapter_empty_text);
        emptyView.setText(getString(R.string.trailers_empty_text));
        mGrid.setEmptyView(emptyView);

        mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.trailers_pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(this);

        mDraggablePanel = (DraggablePanel) view.findViewById(R.id.trailer_draggable_panel);
        mDraggablePanel.setDraggableListener(this);
        mDraggablePanel.setFragmentManager(getChildFragmentManager());
        mDraggablePanel.setTopFragment(mTopFragment);
        mDraggablePanel.setBottomFragment(mBottomFragment);
        mDraggablePanel.initializeView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_trailers, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(getActivity(), TrailerSearchResultsActivity.class)));
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!mIsSelectingNewReminder) {
            if (Utils.isTrailerPanelEnabled(mPrefs)) {
                getActivity().setTitle(mAdapter.getItem(position).getTitle());
                mTopFragment.updateWithTrailer(mAdapter.getItem(position));
                mBottomFragment.updateWithTrailer(mAdapter.getItem(position));
                mDraggablePanel.setVisibility(View.VISIBLE);
                mDraggablePanel.maximize();
            } else {
                startTrailerActivity(mAdapter.getItem(position));
            }
        } else {
            didSelectTrailerForReminder(mAdapter.getItem(position));
        }
    }

    @Override
    public void onFabClick() {
        mIsSelectingNewReminder = !mIsSelectingNewReminder;
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
        getActivity().setTitle(getString(R.string.app_name));
        getExpandableFab().slideOutFab();
        initializeMenu();
    }

    @Override
    public void onClosedToLeft() {
        finishActionMode();
        getActivity().setTitle(getString(R.string.app_name));
        getExpandableFab().slideInFab();
        initializeMenu();
        finishActionMode();
    }

    @Override
    public void onClosedToRight() {
        finishActionMode();
        getActivity().setTitle(getString(R.string.app_name));
        getExpandableFab().slideInFab();
        initializeMenu();
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

    protected void finishActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    protected void didSelectTrailerForReminder(Trailer trailer) {
        mIsSelectingNewReminder = false;
        getExpandableFab().collapseFab(true);
    }

    protected abstract void startTrailerActivity(Trailer trailer);
}
