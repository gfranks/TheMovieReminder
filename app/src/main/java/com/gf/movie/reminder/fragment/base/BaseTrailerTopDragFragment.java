package com.gf.movie.reminder.fragment.base;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.data.model.Trailer;

public abstract class BaseTrailerTopDragFragment extends BaseFragment {

    public static final String EXTRA_IS_PANEL = "is_panel";

    protected Reminder mReminder;
    protected Trailer mTrailer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null || !getArguments().getBoolean(EXTRA_IS_PANEL, false)) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_trailer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void updateWithReminder(Reminder reminder) {
        mReminder = reminder;
        mTrailer = reminder.getTrailer();
    }

    public void updateWithTrailer(Trailer trailer) {
        mReminder = null;
        mTrailer = trailer;
    }

    public Reminder getReminder() {
        return mReminder;
    }

    public Trailer getTrailer() {
        return mTrailer;
    }
}
