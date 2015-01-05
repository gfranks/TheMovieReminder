package com.gf.movie.reminder.fragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.gf.movie.reminder.BuildConfig;
import com.gf.movie.reminder.R;
import com.gf.movie.reminder.adapter.EnumAdapter;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.gf.movie.reminder.util.AppContainerActionBarDrawerToggle;
import com.gf.movie.reminder.util.Strings;
import com.gf.movie.reminder.util.Utils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import retrofit.RestAdapter;

public class DevSettingsFragment extends BaseFragment {

    public static final String TAG = "dev_settings";
    private static final DateFormat DATE_DISPLAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

    @Inject
    Picasso mPicasso;

    @Inject
    RestAdapter mRestAdapter;

    @Inject
    SharedPreferences mPrefs;

    private TextView mBuildNameView;
    private TextView mBuildCodeView;
    private TextView mBuildShaView;
    private TextView mBuildDateView;
    private TextView mDeviceMakeView;
    private TextView mDeviceModelView;
    private TextView mDeviceResolutionView;
    private TextView mDeviceDensityView;
    private TextView mDeviceReleaseView;
    private TextView mDeviceApiView;
    private Spinner mNetworkLoggingView;
    private Spinner mImagesLoggingView;
    private Spinner mImagesIndicatorsView;
    private CheckBox mTransitionAnimation;
    private CheckBox mDrawerLayoutBlur;
    private CheckBox mTrailerPanel;

    private static String getDensityString(DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return "unknown";
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dev_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBuildNameView = (TextView) view.findViewById(R.id.dev_build_name);
        mBuildCodeView = (TextView) view.findViewById(R.id.dev_build_code);
        mBuildShaView = (TextView) view.findViewById(R.id.dev_build_sha);
        mBuildDateView = (TextView) view.findViewById(R.id.dev_build_date);
        mDeviceMakeView = (TextView) view.findViewById(R.id.dev_device_make);
        mDeviceModelView = (TextView) view.findViewById(R.id.dev_device_model);
        mDeviceResolutionView = (TextView) view.findViewById(R.id.dev_device_resolution);
        mDeviceDensityView = (TextView) view.findViewById(R.id.dev_device_density);
        mDeviceReleaseView = (TextView) view.findViewById(R.id.dev_device_release);
        mDeviceApiView = (TextView) view.findViewById(R.id.dev_device_api);

        mNetworkLoggingView = (Spinner) view.findViewById(R.id.dev_network_logging);
        mImagesLoggingView = (Spinner) view.findViewById(R.id.dev_images_logging);
        mImagesIndicatorsView = (Spinner) view.findViewById(R.id.dev_images_indicators);

        mTransitionAnimation = (CheckBox) view.findViewById(R.id.dev_transition_animation);
        mDrawerLayoutBlur = (CheckBox) view.findViewById(R.id.dev_drawer_layout_blur);
        mTrailerPanel = (CheckBox) view.findViewById(R.id.dev_trailer_panel);
    }

    @Override
    public void onResume() {
        super.onResume();

        setupNetworkSection();
        setupImagesSection();
        setupAnimationSection();
        setupDrawerLayoutSection();
        setupAdditionalSection();
        setupBuildSection();
        setupDeviceSection();
    }

    private void setupNetworkSection() {
        // We use the JSON rest adapter as the source of truth for the log level.
        final EnumAdapter<RestAdapter.LogLevel> loggingAdapter = new EnumAdapter<RestAdapter.LogLevel>(getActivity(), RestAdapter.LogLevel.class, R.layout.layout_dev_settings_spinner_item);
        mNetworkLoggingView.setAdapter(loggingAdapter);
        mNetworkLoggingView.setSelection(mRestAdapter.getLogLevel().ordinal());
        mNetworkLoggingView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                RestAdapter.LogLevel selected = loggingAdapter.getItem(position);
                if (selected != mRestAdapter.getLogLevel()) {
                    mRestAdapter.setLogLevel(selected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setupImagesSection() {
        final EnumAdapter<YesNoEnum> loggingAdapter = new EnumAdapter<YesNoEnum>(getActivity(), YesNoEnum.class, R.layout.layout_dev_settings_spinner_item);
        mImagesLoggingView.setAdapter(loggingAdapter);
        mImagesLoggingView.setSelection(mPicasso.isLoggingEnabled() ? 1 : 0);
        mImagesLoggingView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (mPicasso.isLoggingEnabled() && position == 1)
                    return;
                mPicasso.setLoggingEnabled(position == 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final EnumAdapter<YesNoEnum> indicatorsAdapter = new EnumAdapter<YesNoEnum>(getActivity(), YesNoEnum.class, R.layout.layout_dev_settings_spinner_item);
        mImagesIndicatorsView.setAdapter(indicatorsAdapter);
        mImagesIndicatorsView.setSelection(mPicasso.areIndicatorsEnabled() ? 1 : 0);
        mImagesIndicatorsView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (mPicasso.areIndicatorsEnabled() && position == 1)
                    return;
                mPicasso.setIndicatorsEnabled(position == 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setupAnimationSection() {
        if (Utils.isTransitionAnimationEnabled(mPrefs)) {
            mTransitionAnimation.setChecked(true);
        }

        mTransitionAnimation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Utils.setTrantitionAnimationEnabled(mPrefs, isChecked);
            }
        });
    }

    private void setupDrawerLayoutSection() {
        if (Utils.isDrawerLayoutBlurEnabled(mPrefs)) {
            mDrawerLayoutBlur.setChecked(true);
        }

        mDrawerLayoutBlur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((AppContainerActionBarDrawerToggle) getDrawerLayout().getDrawerListener()).setBlurEnabled(isChecked);
                Utils.setDrawerLayoutBlurEnabled(mPrefs, isChecked);
            }
        });
    }

    private void setupAdditionalSection() {
        if (Utils.isTrailerPanelEnabled(mPrefs)) {
            mTrailerPanel.setChecked(true);
        }

        mTrailerPanel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Utils.setTrailerPanelEnabled(mPrefs, isChecked);
            }
        });
    }

    private void setupBuildSection() {
        mBuildNameView.setText(BuildConfig.VERSION_NAME);
        mBuildCodeView.setText(String.valueOf(BuildConfig.VERSION_CODE));
        mBuildShaView.setText(BuildConfig.GIT_SHA);

        try {
            // Parse ISO8601-format time into local time.
            DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            inFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date buildTime = inFormat.parse(BuildConfig.BUILD_TIME);
            mBuildDateView.setText(DATE_DISPLAY_FORMAT.format(buildTime));
        } catch (ParseException e) {
            throw new RuntimeException("Unable to decode build time: " + BuildConfig.BUILD_TIME, e);
        }
    }

    private void setupDeviceSection() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        String densityBucket = getDensityString(displayMetrics);
        mDeviceMakeView.setText(Strings.truncateAt(Build.MANUFACTURER, 20));
        mDeviceModelView.setText(Strings.truncateAt(Build.MODEL, 20));
        mDeviceResolutionView.setText(displayMetrics.heightPixels + "x" + displayMetrics.widthPixels);
        mDeviceDensityView.setText(displayMetrics.densityDpi + "dpi (" + densityBucket + ")");
        mDeviceReleaseView.setText(Build.VERSION.RELEASE);
        mDeviceApiView.setText(String.valueOf(Build.VERSION.SDK_INT));
    }

    private enum YesNoEnum {
        NO,
        YES;
    }
}
