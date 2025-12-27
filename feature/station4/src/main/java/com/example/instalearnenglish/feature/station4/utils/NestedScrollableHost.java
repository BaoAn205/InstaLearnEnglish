package com.example.instalearnenglish.feature.station4.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

/**
 * A custom FrameLayout that solves the nested scrolling conflict between a parent ViewPager2 and a child ViewPager2.
 */
public class NestedScrollableHost extends FrameLayout {
    private int touchSlop;
    private float initialX;
    private float initialY;
    private final ViewPager2 parentViewPager;

    public NestedScrollableHost(@NonNull Context context) {
        super(context);
        this.parentViewPager = findParentViewPager(this);
        init(context);
    }

    public NestedScrollableHost(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.parentViewPager = findParentViewPager(this);
        init(context);
    }

    private void init(Context context) {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private ViewPager2 findParentViewPager(View child) {
        View parent = (View) child.getParent();
        while (parent != null && !(parent instanceof ViewPager2)) {
            parent = (View) parent.getParent();
        }
        return (ViewPager2) parent;
    }

    private boolean canChildScroll(int orientation, float delta) {
        int direction = (int) Math.signum(delta);
        View child = getChildAt(0);
        if (child instanceof ViewPager2) {
            return ((ViewPager2) child).canScrollHorizontally(direction);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (parentViewPager == null) {
            return super.onInterceptTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            initialX = ev.getX();
            initialY = ev.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = ev.getX() - initialX;
            float dy = ev.getY() - initialY;
            boolean isVp2Horizontal = parentViewPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL;

            if (isVp2Horizontal) {
                float absDx = Math.abs(dx);
                if (absDx > touchSlop && absDx > Math.abs(dy)) {
                    if (dx > 0) {
                        // Scrolling left
                        if (canChildScroll(-1, dx)) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        // Scrolling right
                        if (canChildScroll(1, dx)) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
