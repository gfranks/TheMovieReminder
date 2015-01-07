package com.gf.movie.reminder.fragment.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.gf.movie.reminder.adapter.RemindersGridAdapter;
import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.util.Utils;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggablePanel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public abstract class BaseRemindersFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        AbsListView.MultiChoiceModeListener, DraggableListener {

    @Inject
    protected Picasso mPicasso;

    @Inject
    protected SharedPreferences mPrefs;

    protected GridView mGrid;
    protected RemindersGridAdapter mAdapter;
    protected DraggablePanel mDraggablePanel;
    protected ActionMode mActionMode;

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

        mDraggablePanel = (DraggablePanel) view.findViewById(R.id.reminders_draggable_panel);
        mDraggablePanel.setDraggableListener(this);
        mDraggablePanel.setFragmentManager(getChildFragmentManager());
        mDraggablePanel.setTopFragment(mTopFragment);
        mDraggablePanel.setBottomFragment(mBottomFragment);
        mDraggablePanel.initializeView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getReminders();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (Utils.isTrailerPanelEnabled(mPrefs)) {
            getActivity().setTitle(mAdapter.getItem(position).getTrailer().getTitle());
            mTopFragment.updateWithReminder(mAdapter.getItem(position));
            mBottomFragment.updateWithReminder(mAdapter.getItem(position));
            mDraggablePanel.setVisibility(View.VISIBLE);
            mDraggablePanel.maximize();
        } else {
            startRemindersActivity(mAdapter.getItem(position));
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
        clearMenu();
    }

    @Override
    public void onMinimized() {
        finishActionMode();
        getActivity().setTitle(getString(R.string.app_name));
        initializeMenu();
    }

    @Override
    public void onClosedToLeft() {
        finishActionMode();
        getActivity().setTitle(getString(R.string.app_name));
        initializeMenu();
    }

    @Override
    public void onClosedToRight() {
        finishActionMode();
        getActivity().setTitle(getString(R.string.app_name));
        initializeMenu();
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

    protected abstract void getReminders();

    protected abstract void startRemindersActivity(Reminder reminder);
}
