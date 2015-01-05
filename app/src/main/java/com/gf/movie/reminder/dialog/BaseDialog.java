package com.gf.movie.reminder.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.util.Blur;
import com.gf.movie.reminder.util.Utils;

public class BaseDialog extends Dialog implements DialogInterface.OnDismissListener {

    protected BaseActivity mActivity;
    private boolean mBlurBackground;

    public BaseDialog(BaseActivity activity, boolean blurBackground) {
        super(activity, R.style.Base_Theme_AppCompat_Dialog);
        mActivity = activity;
        mBlurBackground = blurBackground;
        setOnDismissListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mBlurBackground) {
            View container = mActivity.findViewById(android.R.id.content);
            Bitmap downScaled = Utils.drawViewToBitmap(container,
                    container.getWidth(), container.getHeight(), 3);
            // apply the blur using the renderscript
            Bitmap blurred = Blur.apply(mActivity, downScaled, 10);
            downScaled.recycle();
            getWindow().setBackgroundDrawable(new BitmapDrawable(getContext().getResources(), blurred));
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (mBlurBackground) {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }
}
