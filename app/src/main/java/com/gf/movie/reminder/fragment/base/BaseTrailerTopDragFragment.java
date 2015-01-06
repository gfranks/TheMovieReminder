package com.gf.movie.reminder.fragment.base;

import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.data.model.Trailer;

public abstract class BaseTrailerTopDragFragment extends BaseFragment {

    public abstract void updateWithReminder(Reminder reminder);

    public abstract void updateWithTrailer(Trailer trailer);
}
