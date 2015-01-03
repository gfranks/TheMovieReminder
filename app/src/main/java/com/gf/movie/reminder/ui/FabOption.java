package com.gf.movie.reminder.ui;

import android.animation.Animator;
import android.view.View;

public class FabOption {

    private final View mView;
    private Type mType;

    public FabOption(final ExpandableFab expandableFab, View view, Type type, final ExpandableFab.OnFabOptionClickListener listener) {
        mView = view;
        mType = type;

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFabOptionClick(expandableFab, FabOption.this);
                }
            }
        });
    }

    public View getView() {
        return mView;
    }

    public Type getType() {
        return mType;
    }

    public int getHeight() {
        return mView.getMeasuredHeight();
    }

    public void expand(boolean animate, float position) {
        if (animate) {
            mView.animate().y(position).alpha(1.0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mView.setVisibility(View.VISIBLE);
                    mView.setAlpha(0.0f);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            mView.setY(position);
            mView.setVisibility(View.VISIBLE);
            mView.setAlpha(1.0f);
        }
    }

    public void collapse(boolean animate, float position) {
        if (animate) {
            mView.animate().y(position).alpha(0.0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mView.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            mView.setY(position);
            mView.setVisibility(View.INVISIBLE);
            mView.setAlpha(0.0f);
        }
    }

    public enum Type {
        MAINTENANCE,
        PACKAGE,
        RENT_PAYMENT,
        CALL,
        EMAIL,
        SCHEDULE_VISIT,
        SMILE_CARD
    }
}
