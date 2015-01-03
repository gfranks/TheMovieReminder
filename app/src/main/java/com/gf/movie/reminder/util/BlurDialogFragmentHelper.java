package com.gf.movie.reminder.util;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.gf.movie.reminder.R;

public class BlurDialogFragmentHelper {

    private static final String TAG = BlurDialogFragmentHelper.class.getSimpleName();

    private DialogFragment mFragment;

    private ViewGroup mRoot;

    private View mBgView;

    private ImageView mBlurImgView;

    private int mAnimDuration;

    private int mWindowAnimResId;

    private int mBgColorResId;

    public BlurDialogFragmentHelper(DialogFragment fragment) {
        mFragment = fragment;
        mAnimDuration = fragment.getActivity().getResources().getInteger(android.R.integer.config_mediumAnimTime);
        mWindowAnimResId = R.style.BlurDialogSlideAnimation;
        mBgColorResId = R.color.theme_glass;
    }

    public void setAnimDuration(int animDuration) {
        mAnimDuration = animDuration;
    }

    public void setWindowAnimResId(int windowAnimResId) {
        mWindowAnimResId = windowAnimResId;
    }

    public void setBgColorResId(int bgColorResId) {
        mBgColorResId = bgColorResId;
    }

    public void onCreate(Bundle savedInstanceState) {
        mFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        Window window = mFragment.getDialog().getWindow();
        window.setWindowAnimations(mWindowAnimResId);
        // set transparent background for the window of this dialog
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mBgView = new View(mFragment.getActivity());
        mBgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mBgView.setBackgroundColor(mFragment.getResources().getColor(mBgColorResId));
        Utils.setAlpha(mBgView, 0f);

        mBlurImgView = new ImageView(mFragment.getActivity());
        mBlurImgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mBlurImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Utils.setAlpha(mBlurImgView, 0f);

        mRoot = (ViewGroup) mFragment.getActivity().getWindow().getDecorView();

        mRoot.addView(mBlurImgView);
        mRoot.addView(mBgView);

        Bitmap bitmap = Utils.drawViewToBitmap(mRoot, mRoot.getWidth(), mRoot.getHeight(), 3);
        Bitmap blurred = Blur.apply(mFragment.getActivity(), bitmap);
        mBlurImgView.setImageBitmap(blurred);
        bitmap.recycle();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFragment.dismiss();
                startExitAnimation();
                return true;
            }
        });
    }

    public void onStart() {
        startEnterAnimation();
    }

    public void onCancel(DialogInterface dialog) {
        startExitAnimation();
    }

    private void startEnterAnimation() {
        Utils.animateAlpha(mBgView, 0f, 1f, mAnimDuration, null);
        Utils.animateAlpha(mBlurImgView, 0f, 1f, mAnimDuration, null);
    }

    private void startExitAnimation() {
        Utils.animateAlpha(mBgView, 1f, 0f, mAnimDuration, null);
        Utils.animateAlpha(mBlurImgView, 1f, 0f, mAnimDuration, new Runnable() {
            @Override
            public void run() {
                mRoot.removeView(mBlurImgView);
                mRoot.removeView(mBgView);
            }
        });
    }
}