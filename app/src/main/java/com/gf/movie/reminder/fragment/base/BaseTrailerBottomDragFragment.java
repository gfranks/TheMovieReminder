package com.gf.movie.reminder.fragment.base;

import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.data.model.Trailer;

public abstract class BaseTrailerBottomDragFragment extends BaseFragment {

    protected Reminder mReminder;
    protected Trailer mTrailer;

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
