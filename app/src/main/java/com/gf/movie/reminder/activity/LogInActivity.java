package com.gf.movie.reminder.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.fragment.LogInFragment;
import com.gf.movie.reminder.fragment.SignUpFragment;

public class LogInActivity extends BaseActivity {

    private final String SIGN_UP_SHOWING = "sign_up_showing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_frame);

        LogInFragment logInFragment = (LogInFragment) getSupportFragmentManager().findFragmentByTag(LogInFragment.TAG);
        if (logInFragment == null) {
            logInFragment = new LogInFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_fragment_content, logInFragment, LogInFragment.TAG)
                .addToBackStack(LogInFragment.TAG)
                .commit();

        if (savedInstanceState != null && savedInstanceState.getBoolean(SIGN_UP_SHOWING, false)) {
            SignUpFragment signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag(SignUpFragment.TAG);
            if (signUpFragment == null) {
                signUpFragment = new SignUpFragment();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_fragment_content, signUpFragment, SignUpFragment.TAG)
                    .addToBackStack(SignUpFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_fragment_content);
        outState.putBoolean(SIGN_UP_SHOWING, fragment != null && fragment instanceof SignUpFragment);
    }

    @Override
    protected void checkBackStackEntryOnBackPress() {
        finish();
    }
}
