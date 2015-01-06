package com.gf.movie.reminder.fragment.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        mDraggablePanel.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_trailers, menu);
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

                mActionMode = getToolbar().startActionMode(new DraggableActionModeCallback(mAdapter.getItem(position).getTitle()));
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
        getExpandableFab().slideOutFab();
        clearMenu();
    }

    @Override
    public void onMinimized() {
        finishActionMode();
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

    protected class DraggableActionModeCallback implements ActionMode.Callback {

        private String mModeTitle;

        public DraggableActionModeCallback(String modeTitle) {
            mModeTitle = modeTitle;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getToolbar().setVisibility(View.GONE);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(mModeTitle);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            getToolbar().setVisibility(View.VISIBLE);
            mDraggablePanel.minimize();
        }
    }
}