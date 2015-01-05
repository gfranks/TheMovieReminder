package com.gf.movie.reminder.fragment.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.ui.AppContainer;
import com.gf.movie.reminder.ui.ExpandableFab;
import com.gf.movie.reminder.util.AccountManager;
import com.gf.movie.reminder.util.BlurDialogFragmentHelper;
import com.gf.movie.reminder.view.FeedbackBar;
import com.gf.movie.reminder.view.MovieReminderDrawerLayout;

import javax.inject.Inject;

public class BaseDialogFragment extends DialogFragment {

    public static final int POSITIVE_BUTTON = 0;
    public static final int NEGATIVE_BUTTON = 1;

    @Inject
    AccountManager mAccountManager;

    private BlurDialogFragmentHelper mHelper;

    private TextView mTitle;
    private TextView mMessage;
    private View mButtonContainer;
    private Button mPositiveButton;
    private Button mNegativeButton;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new BlurDialogFragmentHelper(this);
        mHelper.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, int layoutResID) {
        View view = inflater.inflate(R.layout.dialog_fragment_base, container, false);

        ViewGroup content = ((ViewGroup) view.findViewById(R.id.dialog_content));
        inflater.inflate(layoutResID, content);

        mTitle = (TextView) view.findViewById(R.id.dialog_title);
        mMessage = (TextView) view.findViewById(R.id.dialog_message);
        mButtonContainer = view.findViewById(R.id.dialog_button_container);
        mPositiveButton = (Button) view.findViewById(R.id.dialog_button_positive);
        mNegativeButton = (Button) view.findViewById(R.id.dialog_button_negative);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHelper.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHelper.onStart();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mHelper.onCancel(dialog);
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mHelper.onDismiss(dialog);
        super.onDismiss(dialog);
    }

    public void setTitle(int stringResId) {
        setTitle(getString(stringResId));
    }

    public void setTitle(String text) {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(text);
    }

    public void setMessage(int stringResId) {
        setMessage(getString(stringResId));
    }

    public void setMessage(String text) {
        mMessage.setVisibility(View.VISIBLE);
        mMessage.setText(text);
    }

    public void setPositiveButton(int stringResId, OnClickListener listener) {
        setPositiveButton(getString(stringResId), listener);
    }

    public void setPositiveButton(String text, final OnClickListener listener) {
        setPositiveButtonVisibility(View.VISIBLE);
        mPositiveButton.setText(text);
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, POSITIVE_BUTTON);
            }
        });
    }

    public void setPositiveButtonVisibility(int visibility) {
        if ((visibility == View.GONE || visibility == View.INVISIBLE) && (mNegativeButton.getVisibility() == View.GONE || mNegativeButton.getVisibility() == View.INVISIBLE)) {
            mButtonContainer.setVisibility(View.GONE);
        } else {
            mButtonContainer.setVisibility(View.VISIBLE);
            mPositiveButton.setVisibility(visibility);
        }
    }

    public void setNegativeButton(int stringResId, OnClickListener listener) {
        setNegativeButton(getString(stringResId), listener);
    }

    public void setNegativeButton(String text, final OnClickListener listener) {
        setNegativeButtonVisibility(View.VISIBLE);
        mNegativeButton.setText(text);
        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, NEGATIVE_BUTTON);
            }
        });
    }

    public void setNegativeButtonVisibility(int visibility) {
        if ((visibility == View.GONE || visibility == View.INVISIBLE) && (mPositiveButton.getVisibility() == View.GONE || mPositiveButton.getVisibility() == View.INVISIBLE)) {
            mButtonContainer.setVisibility(View.GONE);
        } else {
            mButtonContainer.setVisibility(View.VISIBLE);
            mNegativeButton.setVisibility(visibility);
        }
    }

    public AccountManager getAccountManager() {
        return mAccountManager;
    }

    public AppContainer getAppContainer() {
        return ((BaseActivity) getActivity()).getAppContainer();
    }

    public Toolbar getToolbar() {
        return ((BaseActivity) getActivity()).getToolbar();
    }

    public MovieReminderDrawerLayout getDrawerLayout() {
        return ((BaseActivity) getActivity()).getDrawerLayout();
    }

    public FeedbackBar getFeedbackBar() {
        return ((BaseActivity) getActivity()).getFeedbackBar();
    }

    public ExpandableFab getExpandableFab() {
        return ((BaseActivity) getActivity()).getExpandableFab();
    }

    public interface OnClickListener {
        void onClick(View v, int which);
    }
}
