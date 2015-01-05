package com.gf.movie.reminder.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.fragment.base.BaseDialogFragment;

public class DeveloperToggleDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private final String DEVOPER_ENABLE_STRING = "@LGFZ71";

    private EditText mInput;
    private boolean mDevSettingsEnabled;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_developer_toggle, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInput = (EditText) view.findViewById(R.id.dev_toggle_text);
        view.findViewById(R.id.dev_toggle_cancel).setOnClickListener(this);
        view.findViewById(R.id.dev_toggle_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dev_toggle_ok) {
            if (mInput.getText().toString().equals(DEVOPER_ENABLE_STRING)) {
                dismiss();
                getActivity().getSharedPreferences(MovieReminderApplication.TAG, Context.MODE_PRIVATE).edit().putBoolean("DEV_SETTINGS_ENABLED", true).apply();
                getAppContainer().addDevSettingsDrawerComponent((BaseActivity) getActivity());
                mDevSettingsEnabled = true;
            } else {
                mInput.setError(getString(R.string.dev_toggle_dialog_error));
            }
        } else if (v.getId() == R.id.dev_toggle_cancel) {
            dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mDevSettingsEnabled) {
            getFeedbackBar().showInfo(R.string.dev_toggle_dialog_success, false);
        }
    }

    public void showFromFragment(Fragment fragment) {
        show(fragment.getChildFragmentManager(), "dev_toggle");
    }

    public void showFromActivity(BaseActivity activity) {
        show(activity.getSupportFragmentManager(), "dev_toggle");
    }
}
