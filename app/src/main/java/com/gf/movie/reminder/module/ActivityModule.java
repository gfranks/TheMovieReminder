package com.gf.movie.reminder.module;

import android.content.Context;

import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.adapter.NavigationListAdapter;
import com.gf.movie.reminder.adapter.TrailersGridAdapter;
import com.gf.movie.reminder.fragment.DevSettingsFragment;
import com.gf.movie.reminder.fragment.DeveloperToggleDialogFragment;
import com.gf.movie.reminder.fragment.LogInFragment;
import com.gf.movie.reminder.fragment.MovieTrailerBottomDragFragment;
import com.gf.movie.reminder.fragment.MovieTrailerTopDragFragment;
import com.gf.movie.reminder.fragment.NavigationFragment;
import com.gf.movie.reminder.fragment.RemindersFragment;
import com.gf.movie.reminder.fragment.SignUpFragment;
import com.gf.movie.reminder.fragment.TrailersFragment;
import com.gf.movie.reminder.fragment.base.BaseDialogFragment;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.gf.movie.reminder.ui.ForActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                BaseFragment.class,
                BaseDialogFragment.class,
                DevSettingsFragment.class,
                DeveloperToggleDialogFragment.class,
                NavigationFragment.class,
                NavigationListAdapter.class,
                TrailersFragment.class,
                TrailersGridAdapter.class,
                RemindersFragment.class,
                MovieTrailerTopDragFragment.class,
                MovieTrailerBottomDragFragment.class,
                LogInFragment.class,
                SignUpFragment.class
        },
        addsTo = MovieReminderModule.class,
        library = true,
        complete = false
)
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    /**
     * Allow the activity context to be injected but require that it be annotated with
     * {@link com.gf.movie.reminder.ui.ForActivity @ForActivity} to explicitly differentiate it from application context.
     */
    @Provides
    @Singleton
    @ForActivity
    Context provideActivityContext() {
        return activity;
    }
}
