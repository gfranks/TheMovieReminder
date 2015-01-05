package com.gf.movie.reminder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gf.movie.reminder.BuildConfig;
import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.LogInActivity;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.adapter.NavigationListAdapter;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.gf.movie.reminder.ui.NavigationListItem;
import com.gf.movie.reminder.util.AccountManager;
import com.gf.movie.reminder.util.AppContainerActionBarDrawerToggleListener;

import javax.inject.Inject;

public class NavigationFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, AppContainerActionBarDrawerToggleListener {

    public static final String TAG = "navigation";
    @Inject
    AccountManager mAccountManager;
    private int mCopyrightClickCount;
    private ListView mListView;
    private NavigationListAdapter mAdapter;
    private TextView mCopyrightLabel;
    private boolean mShowDevToggleDialog;

    private OnNavigationItemSelectedListener mListener;

    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = (ListView) view.findViewById(R.id.navigation_list);
        mListView.setOnItemClickListener(this);
        mAdapter = new NavigationListAdapter((BaseActivity) getActivity());
        mListView.setAdapter(mAdapter);

        mCopyrightLabel = (TextView) view.findViewById(R.id.copyright);
        mCopyrightLabel.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NavigationListItem item = NavigationListItem.values()[position];

        switch (item) {
            case MOVIE_TRAILERS:
                break;
            case MOVIE_REMINDERS:
                break;
            case GAME_TRAILERS:
                break;
            case GAME_REMINDERS:
                break;
            case LOGIN:
                if (mAccountManager.isLoggedIn()) {
                    // logout
                    mAccountManager.logout();
                    mAdapter.notifyDataSetChanged();
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LogInActivity.class));
                }
                break;
            default:
                break;
        }

        if (mListener != null) {
            mListener.onItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (!BuildConfig.DEBUG) {
            ++mCopyrightClickCount;
            if (mCopyrightClickCount == 15) {
                mCopyrightClickCount = 0;
                mShowDevToggleDialog = true;
                if (mListener != null) {
                    mListener.onItemSelected(NavigationListItem.COPYRIGHT);
                }
            }
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (mShowDevToggleDialog) {
            mShowDevToggleDialog = false;
            new DeveloperToggleDialogFragment().showFromActivity((BaseActivity) getActivity());
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    public interface OnNavigationItemSelectedListener {
        void onItemSelected(NavigationListItem item);
    }
}
