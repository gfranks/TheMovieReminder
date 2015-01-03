package com.gf.movie.reminder.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.util.Utils;

public class Fab extends ImageButton {

    public Fab(Context context) {
        super(context);
        init();
    }

    public Fab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Fab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (Utils.isAtLeastLollipop()) {
            setElevation(getResources().getDimensionPixelSize(R.dimen.fab_elevation));
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int diameter = getMeasuredWidth();
                    outline.setOval(0, 0, diameter, diameter);
                }
            });
            setClipToOutline(true);
        }
    }

    public void animateVisibility(final float fromAlpha, final float toAlpha, final int visibility) {
        setAlpha(fromAlpha);
        animate().setDuration(2000).alpha(toAlpha).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (toAlpha > fromAlpha) {
                    setVisibility(visibility);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (toAlpha < fromAlpha) {
                    setVisibility(visibility);
                }
            }
        });
    }
}
