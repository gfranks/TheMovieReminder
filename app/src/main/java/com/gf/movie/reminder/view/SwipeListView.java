package com.gf.movie.reminder.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gf.movie.reminder.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwipeListView extends ListView implements View.OnTouchListener {

    // Cached ViewConfiguration and system-wide constant values
    private int mSlop;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;
    private long mAnimationTime;

    // Fixed properties
    private SwipeCallbacks mCallbacks;
    private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero

    // Transient properties
    private List<PendingDismissData> mPendingDismisses = new ArrayList<PendingDismissData>();
    private int mDismissAnimationRefCount = 0;
    private float mDownX;
    private boolean mSwiping;
    private VelocityTracker mVelocityTracker;
    private int mCurrentPosition;
    private int mDismissViewResId = -1;
    private View mCurrentView;
    private int mLeftActionResId = -1;
    private int mRightActionResId = -1;
    private boolean mShouldSnapToAction;
    private boolean mIsSnappedToAction;
    private boolean mPaused;

    private OnScrollListener mOnScrollListenerDelegate;
    private OnTouchListener mTouchListenerDelegate;

    public SwipeListView(Context context) {
        super(context);
        init(null);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity() * 16;
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        mAnimationTime = getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
        super.setOnTouchListener(this);
        super.setOnScrollListener(makeScrollListener());

        if (attrs != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.SwipeListView);
            mDismissViewResId = styled.getResourceId(R.styleable.SwipeListView_dismissView, -1);
            mLeftActionResId = styled.getResourceId(R.styleable.SwipeListView_leftActionView, -1);
            mRightActionResId = styled.getResourceId(R.styleable.SwipeListView_rightActionView, -1);
            mShouldSnapToAction = styled.getBoolean(R.styleable.SwipeListView_snapToAction, false);
            styled.recycle();
        }
    }

    public void setDismissViewResId(int dismissViewResId) {
        this.mDismissViewResId = dismissViewResId;
    }

    public void setRightActionResId(int rightActionResId) {
        this.mRightActionResId = rightActionResId;
    }

    public void setLeftActionResId(int leftActionResId) {
        this.mLeftActionResId = leftActionResId;
    }

    public void setShouldSnapToAction(boolean shouldSnapToAction) {
        this.mShouldSnapToAction = shouldSnapToAction;
    }

    public void setSwipeCallback(SwipeCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void setOnTouchListener(OnTouchListener touchListenerDelegate) {
        mTouchListenerDelegate = touchListenerDelegate;
    }

    /**
     * Enables or disables (pauses or resumes) watching for swipe-to-dismiss gestures.
     *
     * @param enabled Whether or not to watch for gestures.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mPaused = !enabled;
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListenerDelegate = onScrollListener;
    }

    /**
     * Returns an {@link android.widget.AbsListView.OnScrollListener} to be added to the {@link
     * android.widget.ListView} using {@link android.widget.ListView#setOnScrollListener(android.widget.AbsListView.OnScrollListener)}.
     * If a scroll listener is already assigned, the caller should still pass scroll changes through
     * to this listener. This will ensure that this {@link com.rentpath.maxleases.view.SwipeListView} is
     * paused during list view scrolling.</p>
     *
     * @see com.rentpath.maxleases.view.SwipeListView
     */
    public OnScrollListener makeScrollListener() {
        return new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (mOnScrollListenerDelegate != null) {
                    mOnScrollListenerDelegate.onScrollStateChanged(absListView, scrollState);
                }
                setEnabled(scrollState != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (mOnScrollListenerDelegate != null) {
                    mOnScrollListenerDelegate.onScroll(absListView, i, i1, i2);
                }
            }
        };
    }

    /**
     * Manually cause the item at the given position to be dismissed (trigger the dismiss
     * animation).
     */
    public void dismiss(int position) {
        dismiss(getViewForPosition(position), position, true);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mTouchListenerDelegate != null) {
            mTouchListenerDelegate.onTouch(view, motionEvent);
        }

        if (mViewWidth < 2) {
            mViewWidth = getWidth();
        }

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                if (mPaused) {
                    return false;
                }

                // TODO: ensure this is a finger, and set a flag

                // Find the child view that was touched (perform a hit test)
                Rect rect = new Rect();
                int childCount = getChildCount();
                int[] listViewCoords = new int[2];
                getLocationOnScreen(listViewCoords);
                int x = (int) motionEvent.getRawX() - listViewCoords[0];
                int y = (int) motionEvent.getRawY() - listViewCoords[1];
                View child;
                for (int i = 0; i < childCount; i++) {
                    child = getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                        mCurrentView = child;
                        break;
                    }
                }

                if (mCurrentView != null) {
                    mDownX = motionEvent.getRawX();
                    if (mIsSnappedToAction && mDismissViewResId != -1) {
                        if (mCurrentView.findViewById(mDismissViewResId).getTranslationX() > 0) {
                            mDownX -= Math.abs(mCurrentView.findViewById(mDismissViewResId).getTranslationX());
                        } else {
                            mDownX -= mCurrentView.findViewById(mDismissViewResId).getTranslationX();
                        }
                    }
                    mCurrentPosition = getPositionForView(mCurrentView);
                    if (mCallbacks.canSwipe(mCurrentPosition)) {
                        mVelocityTracker = VelocityTracker.obtain();
                        mVelocityTracker.addMovement(motionEvent);
                    } else {
                        mCurrentView = null;
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }

            case MotionEvent.ACTION_UP: {
                if (mVelocityTracker == null) {
                    break;
                }

                float deltaX = motionEvent.getRawX() - mDownX;
                mVelocityTracker.addMovement(motionEvent);
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityX = mVelocityTracker.getXVelocity();
                float absVelocityX = Math.abs(velocityX);
                float absVelocityY = Math.abs(mVelocityTracker.getYVelocity());
                boolean dismiss = false;
                boolean dismissRight = false;
                if (Math.abs(deltaX) > mViewWidth / 2) {
                    dismiss = true;
                    dismissRight = deltaX > 0;
                } else if (mMinFlingVelocity <= absVelocityX && absVelocityX <= mMaxFlingVelocity
                        && absVelocityY < absVelocityX) {
                    // dismiss only if flinging in the same direction as dragging
                    dismiss = (velocityX < 0) == (deltaX < 0);
                    dismissRight = mVelocityTracker.getXVelocity() > 0;
                }
                if (dismiss) {
                    // dismiss
                    mIsSnappedToAction = false;
                    dismiss(mCurrentView, mCurrentPosition, dismissRight);
                } else {
                    // cancel
                    if (mCurrentView != null) {
                        View viewToAnimate = mCurrentView;
                        if (mDismissViewResId != -1) {
                            viewToAnimate = mCurrentView.findViewById(mDismissViewResId);
                        }
                        float translationX = 0;
                        if (mShouldSnapToAction && Math.abs(deltaX) > mViewWidth / 4) {
                            if (deltaX > 0.0) {
                                translationX = mViewWidth / 4;
                            } else {
                                translationX = -(mViewWidth / 4);
                            }
                            mIsSnappedToAction = true;
                        } else {
                            mIsSnappedToAction = false;
                        }
                        viewToAnimate.animate()
                                .translationX(translationX)
                                .alpha(1)
                                .setDuration(mAnimationTime)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        if (mCurrentView != null && mLeftActionResId != -1) {
                                            mCurrentView.findViewById(mLeftActionResId).setAlpha(1);
                                        }
                                        if (mCurrentView != null && mRightActionResId != -1) {
                                            mCurrentView.findViewById(mRightActionResId).setAlpha(1);
                                        }
                                        mCurrentView = null;
                                    }
                                });
                    }
                }
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                mDownX = 0;
                mCurrentPosition = ListView.INVALID_POSITION;
                mSwiping = false;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                if (mVelocityTracker == null) {
                    break;
                }

                if (mCurrentView != null) {
                    // cancel
                    View viewToAnimate = mCurrentView;
                    if (mDismissViewResId != -1) {
                        viewToAnimate = mCurrentView.findViewById(mDismissViewResId);
                    }
                    viewToAnimate.animate()
                            .translationX(0)
                            .alpha(1)
                            .setDuration(mAnimationTime)
                            .setListener(null);
                }
                if (mLeftActionResId != -1) {
                    mCurrentView.findViewById(mLeftActionResId).setAlpha(1);
                }
                if (mRightActionResId != -1) {
                    mCurrentView.findViewById(mRightActionResId).setAlpha(1);
                }
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                mDownX = 0;
                mCurrentView = null;
                mCurrentPosition = ListView.INVALID_POSITION;
                mSwiping = false;
                mIsSnappedToAction = false;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (mVelocityTracker == null || mPaused) {
                    break;
                }

                mVelocityTracker.addMovement(motionEvent);
                float deltaX = motionEvent.getRawX() - mDownX;
                if (Math.abs(deltaX) > mSlop) {
                    mSwiping = true;
                    requestDisallowInterceptTouchEvent(true);
                }
                if (Math.abs(deltaX) > mSlop / 2) {
                    // Cancel ListView's touch (un-highlighting the item)
                    MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                            (motionEvent.getActionIndex()
                                    << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    onTouchEvent(cancelEvent);
                    cancelEvent.recycle();
                }

                if (mSwiping) {
                    if (deltaX > 0) {
                        if (mRightActionResId != -1) {
                            mCurrentView.findViewById(mRightActionResId).setAlpha(0);
                        }
                        if (mLeftActionResId != -1) {
                            mCurrentView.findViewById(mLeftActionResId).setAlpha(1);
                        }
                    } else {
                        if (mRightActionResId != -1) {
                            mCurrentView.findViewById(mRightActionResId).setAlpha(1);
                        }
                        if (mLeftActionResId != -1) {
                            mCurrentView.findViewById(mLeftActionResId).setAlpha(0);
                        }
                    }
                    View viewToAnimate = mCurrentView;
                    float alphaChange = Math.max(0.15f, Math.min(1f,
                            1f - 2f * Math.abs(deltaX) / mViewWidth));
                    if (mDismissViewResId != -1) {
                        viewToAnimate = mCurrentView.findViewById(mDismissViewResId);
                        alphaChange = 1;
                    }
                    viewToAnimate.setTranslationX(deltaX);
                    viewToAnimate.setAlpha(alphaChange);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private void dismiss(final View view, final int position, final boolean swipeRight) {
        ++mDismissAnimationRefCount;
        if (view == null) {
            // No view, shortcut to calling onDismiss to let it deal with adapter
            // updates and all that.
            if (swipeRight) {
                mCallbacks.onSwipeActionLeft(this, new int[]{position});
            } else {
                mCallbacks.onSwipeActionRight(this, new int[]{position});
            }
            return;
        }

        View viewToAnimate = view;
        float alphaChange = 0;
        if (mDismissViewResId != -1) {
            viewToAnimate = view.findViewById(mDismissViewResId);
            alphaChange = 1;
        }
        final float alpha = alphaChange;
        viewToAnimate.animate()
                .translationX(swipeRight ? mViewWidth : -mViewWidth)
                .alpha(alpha)
                .setDuration(mAnimationTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        performDismiss(view, position, swipeRight);
                    }
                });
    }

    private View getViewForPosition(int position) {
        int index = position
                - (getFirstVisiblePosition() - getHeaderViewsCount());
        return (index >= 0 && index < getChildCount())
                ? getChildAt(index)
                : null;
    }

    private void performDismiss(final View dismissView, final int dismissPosition, final boolean swipeRight) {
        // Animate the dismissed list item to zero-height and fire the dismiss callback when
        // all dismissed list item animations have completed. This triggers layout on each animation
        // frame; in the future we may want to do something smarter and more performant.

        final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
        final int originalHeight = dismissView.getHeight();

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(mAnimationTime);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                --mDismissAnimationRefCount;
                if (mDismissViewResId != -1) {
                    dismissView.findViewById(mDismissViewResId).setTranslationX(0);
                } else {
                    dismissView.setTranslationX(0);
                }

                if (mRightActionResId != -1) {
                    dismissView.findViewById(mRightActionResId).setAlpha(1);
                }
                if (mLeftActionResId != -1) {
                    dismissView.findViewById(mLeftActionResId).setAlpha(1);
                }
                if (mDismissAnimationRefCount == 0) {
                    // No active animations, process all pending dismisses.
                    // Sort by descending position
                    Collections.sort(mPendingDismisses);

                    int[] dismissPositions = new int[mPendingDismisses.size()];
                    for (int i = mPendingDismisses.size() - 1; i >= 0; i--) {
                        dismissPositions[i] = mPendingDismisses.get(i).position;
                    }
                    if (swipeRight) {
                        mCallbacks.onSwipeActionLeft(SwipeListView.this, dismissPositions);
                    } else {
                        mCallbacks.onSwipeActionRight(SwipeListView.this, dismissPositions);
                    }

                    ViewGroup.LayoutParams lp;
                    for (PendingDismissData pendingDismiss : mPendingDismisses) {
                        // Reset view presentation
                        pendingDismiss.view.setAlpha(1f);
                        pendingDismiss.view.setTranslationX(0);
                        lp = pendingDismiss.view.getLayoutParams();
                        lp.height = originalHeight;
                        pendingDismiss.view.setLayoutParams(lp);
                    }

                    mPendingDismisses.clear();
                }
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                dismissView.setLayoutParams(lp);
            }
        });

        mPendingDismisses.add(new PendingDismissData(dismissPosition, dismissView));
        animator.start();
    }

    /**
     * The callback interface used by {@link com.rentpath.maxleases.view.SwipeListView} to inform its client
     * about a successful dismissal of one or more list item positions.
     */
    public interface SwipeCallbacks {

        /**
         * Called to determine whether the given position can be dismissed.
         */
        boolean canSwipe(int position);

        /**
         * Called when the user has indicated they she would like to dismiss one or more list item when swiping right
         * positions.
         *
         * @param listView               The originating {@link android.widget.ListView}.
         * @param reverseSortedPositions An array of positions to dismiss, sorted in descending
         *                               order for convenience.
         */
        void onSwipeActionLeft(ListView listView, int[] reverseSortedPositions);

        /**
         * Called when the user has indicated they she would like to dismiss one or more list item when swiping left
         * positions.
         *
         * @param listView               The originating {@link android.widget.ListView}.
         * @param reverseSortedPositions An array of positions to dismiss, sorted in descending
         *                               order for convenience.
         */
        void onSwipeActionRight(ListView listView, int[] reverseSortedPositions);
    }

    class PendingDismissData implements Comparable<PendingDismissData> {
        public int position;
        public View view;

        public PendingDismissData(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public int compareTo(PendingDismissData other) {
            // Sort by descending position
            return other.position - position;
        }
    }
}
