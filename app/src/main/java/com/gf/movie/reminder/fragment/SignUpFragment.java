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

public class SignUpFragment extends BaseFragment implements View.OnClickListener, Callback<MovieReminderSession> {

    public static final String TAG = "sign_up";

    @Inject
    RequestService mRequestService;

    private EditText mFirstname;
    private EditText mLastname;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLogIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirstname = (EditText) view.findViewById(R.id.firstname_edit);
        mLastname = (EditText) view.findViewById(R.id.lastname_edit);
        mEmail = (EditText) view.findViewById(R.id.email_edit);
        mPassword = (EditText) view.findViewById(R.id.password_edit);
        mLogIn = (Button) view.findViewById(R.id.log_in_now);
        mLogIn.setOnClickListener(this);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                signUp();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.sign_up));
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
            signUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mLogIn.getId()) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void signUp() {
        if (signUpValid()) {
            //go to the network and sign up, store a token that is returned.
            //uninstall the app in order to rid yourself of the token
            HashMap<String, String> creds = new HashMap<String, String>();
            creds.put("firstname", mFirstname.getText().toString());
            creds.put("lastname", mLastname.getText().toString());
            creds.put("email", mEmail.getText().toString());
            creds.put("password", mPassword.getText().toString());
            mRequestService.register(creds, this);
        } else {
            ((BaseActivity) getActivity()).getFeedbackBar().showError(getString(R.string.signup_invalid), false, FeedbackBar.LENGTH_LONG);
        }
    }

    private boolean signUpValid() {
        return isEmailAddress()
                && mFirstname.getText().toString().length() > 1
                && mLastname.getText().toString().length() > 1
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
