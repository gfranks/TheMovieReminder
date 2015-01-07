package com.gf.movie.reminder.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.util.Utils;

import java.util.ArrayList;

public class ExpandableFab extends FrameLayout {

    private final static int DEFAULT_SIZE = 10;

    private Fab mFab;
    private int mFabResId;
    private boolean mFadeOnFabClick;
    private boolean mFabExpanded;
    private boolean mIsExpandable;
    private ArrayList<FabOption> mFabOptions;

    private OnFabOptionClickListener mListener;
    private OnClickListener mOnFabClickListenerDelegate;

    private boolean mIsSlidOut;

    public ExpandableFab(Context context) {
        super(context);
        init(null);
    }

    public ExpandableFab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ExpandableFab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mFabOptions = new ArrayList<>(DEFAULT_SIZE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(getResources().getDrawable(R.drawable.bg_expandable_fab_transition));
        } else {
            setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_expandable_fab_transition));
        }

        if (attrs != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableFab);
            mFabResId = styled.getResourceId(R.styleable.ExpandableFab_fab, -1);
            mFadeOnFabClick = styled.getBoolean(R.styleable.ExpandableFab_fadeOnFabClick, false);
            styled.recycle();
        }

        mIsExpandable = true;
    }

    public void setFadeOnFabClick(boolean fadeOnFabClick) {
        mFadeOnFabClick = fadeOnFabClick;
    }

    public void setIsExpandable(boolean isExpandable) {
        mIsExpandable = isExpandable;
    }

    public void setOnFabClickListener(OnClickListener onFabClickListenerDelegate) {
        mOnFabClickListenerDelegate = onFabClickListenerDelegate;
    }

    public void setOnFabOptionClickListener(OnFabOptionClickListener listener) {
        mListener = listener;
    }

    public boolean isViewFabView(View v) {
        return v.getId() == mFab.getId();
    }

    public Fab getFab() {
        if (mFab != null) {
            return mFab;
        } else {
            return (Fab) findViewById(mFabResId);
        }
    }

    private void initFab() {
        LayoutParams lp;
        if (findViewById(mFabResId) != null) {
            mFab = (Fab) findViewById(mFabResId);
            lp = (LayoutParams) mFab.getLayoutParams();
            lp.setMargins(0, 0,
                    getResources().getDimensionPixelSize(R.dimen.fab_margin),
                    getResources().getDimensionPixelSize(R.dimen.fab_margin));
            mFab.setLayoutParams(lp);
        } else {
            throw new IllegalStateException("ExpandableFab must contain an ImageButton as the fab and you must pass the reference to this resource within the ExpandableFab xml declaration");
        }

        mFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsExpandable) {
                    mFabExpanded = !mFabExpanded;
                    if (mFabExpanded) {
                        expandFab(true);
                    } else {
                        collapseFab(true);
                    }
                }

                if (mOnFabClickListenerDelegate != null) {
                    mOnFabClickListenerDelegate.onClick(v);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        // override onClick. Setting this will take focus from underlying views
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mFab == null) {
            initFab();
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

    public void addTextOption(String text, Drawable icon, FabOption.Type type) {
        addTextOption(text, getResources().getDrawable(R.drawable.bg_fab_text_option), icon, type);
    }

    public void addTextOption(String text, Drawable background, Drawable icon, FabOption.Type type) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fab_text_option, null);
        LayoutParams lp = new LayoutParams(getResources().getDisplayMetrics().widthPixels / 2,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.END);
        lp.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.fab_margin), 0);
        view.setLayoutParams(lp);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
        TextView textView = (TextView) view.findViewById(R.id.fab_option_text);
        textView.setText(text);
        ImageView imageView = (ImageView) view.findViewById(R.id.fab_option_image);
        imageView.setImageDrawable(icon);
        view.setVisibility(View.INVISIBLE);

        addView(view, 0);
        mFabOptions.add(new FabOption(this, view, type, mListener));
    }

    public void addImageOption(Drawable icon, FabOption.Type type) {
        addImageOption(null, icon, type);
    }

    public void addImageOption(Drawable background, Drawable icon, FabOption.Type type) {
        ImageButton ib = (ImageButton) LayoutInflater.from(getContext()).inflate(R.layout.fab_image_option, null);
        LayoutParams lp = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.fab_small_size),
                getResources().getDimensionPixelSize(R.dimen.fab_small_size),
                Gravity.BOTTOM | Gravity.END);
        lp.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.fab_margin), 0);
        ib.setLayoutParams(lp);

        if (background == null) {
            background = getResources().getDrawable(R.drawable.bg_fab_image_option);
            if (Utils.isAtLeastLollipop()) {
                ib.setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        int diameter = getResources().getDimensionPixelSize(R.dimen.fab_small_size);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                });
                ib.setClipToOutline(true);
            }
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            ib.setBackground(background);
        } else {
            ib.setBackgroundDrawable(background);
        }
        ib.setImageDrawable(icon);
        ib.setVisibility(View.INVISIBLE);

        addView(ib, 0);
        mFabOptions.add(new FabOption(this, ib, type, mListener));
    }

    public void expandFab(boolean animate) {
        mFabExpanded = true;
        if (mFadeOnFabClick) {
            TransitionDrawable transition = (TransitionDrawable) getBackground();
            transition.startTransition(getResources().getInteger(android.R.integer.config_shortAnimTime));
        }
        if (mIsExpandable) {
            if (animate) {
                mFab.animate().rotationBy(45.0f);
            } else {
                mFab.animate().rotationBy(45.0f).setDuration(0);
            }
        }

        if (mFabOptions.size() > 0) {
            float translationFactor = mFabOptions.get(0).getHeight() + 8.0f;
            float initialPosition = mFab.getY();

            for (int i = 0; i < mFabOptions.size(); i++) {
                mFabOptions.get(i).expand(animate, initialPosition - (translationFactor * (i + 1)));
            }
        }
    }

    public void collapseFab(boolean animate) {
        mFabExpanded = false;
        if (mFadeOnFabClick) {
            TransitionDrawable transition = (TransitionDrawable) getBackground();
            transition.reverseTransition(getResources().getInteger(android.R.integer.config_shortAnimTime));
        }
        if (mIsExpandable) {
            if (animate) {
                mFab.animate().rotationBy(-45.0f);
            } else {
                mFab.animate().rotationBy(-45.0f).setDuration(0);
            }
        }

        if (mFabOptions.size() > 0) {
            float position = mFab.getY();
            for (int i = 0; i < mFabOptions.size(); i++) {
                mFabOptions.get(i).collapse(animate, position);
            }
        }
    }

    public void slideOutFab() {
        if (mIsSlidOut) {
            return;
        }
        if (mFabExpanded) {
            collapseFab(true);
            mFabExpanded = false;
        }

        Animation slide_out = AnimationUtils.loadAnimation(getContext(), R.anim.fab_slide_out);
        slide_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsSlidOut = true;
                mFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(slide_out);
    }

    public void slideInFab() {
        if (!mIsSlidOut) {
            return;
        }
        Animation slide_in = AnimationUtils.loadAnimation(getContext(), R.anim.fab_slide_in);
        slide_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsSlidOut = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(slide_in);
    }

    public interface OnFabOptionClickListener {
        void onFabOptionClick(ExpandableFab expandableFab, FabOption option);
    }
}
