package com.gf.movie.reminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.data.api.RequestService;
import com.gf.movie.reminder.data.model.MovieReminderSession;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.gf.movie.reminder.view.FeedbackBar;

import java.util.HashMap;
import java.util.regex.Pattern;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogInFragment extends BaseFragment implements View.OnClickListener, Callback<MovieReminderSession> {

    public static final String TAG = "login";

    @Inject
    RequestService mRequestService;

    private EditText mEmail;
    private EditText mPassword;
    private Button mCreateAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmail = (EditText) view.findViewById(R.id.email_edit);
        mPassword = (EditText) view.findViewById(R.id.password_edit);
        mCreateAccount = (Button) view.findViewById(R.id.create_account);
        mCreateAccount.setOnClickListener(this);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                login();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.login));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_log_in, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem item = menu.findItem(R.id.action_log_in);
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_log_in) {
            login();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mCreateAccount.getId()) {
            // create account
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(getId(), new SignUpFragment(), SignUpFragment.TAG)
                    .addToBackStack(SignUpFragment.TAG)
                    .commit();
        }
    }

    private void login() {
        if (loginValid()) {
            //go to the network and login, store a token that is returned.
            //uninstall the app in order to rid yourself of the token
            HashMap<String, String> creds = new HashMap<String, String>();
            creds.put("email", mEmail.getText().toString());
            creds.put("password", mPassword.getText().toString());
            mRequestService.login(creds, this);
        } else {
            ((BaseActivity) getActivity()).getFeedbackBar().showError(getString(R.string.login_invalid), false, FeedbackBar.LENGTH_LONG);
        }
    }

    private boolean loginValid() {
        return isEmailAddress()
                && mPassword.getText().toString().length() > 1;
    }

    private boolean isEmailAddress() {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(mEmail.getText().toString()).matches();
    }

    @Override
    public void success(MovieReminderSession session, Response response) {
        getAccountManager().login(session);
        getActivity().finish();
    }

    @Override
    public void failure(RetrofitError error) {
        // error
        ((BaseActivity) getActivity()).getFeedbackBar().showError(error.getMessage(), false, FeedbackBar.LENGTH_LONG);
    }
}
