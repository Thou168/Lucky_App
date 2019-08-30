package com.bt_121shoppe.motorbike.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NonScrollableVP extends ViewPager {
    public NonScrollableVP(@NonNull Context context) {
        super(context);
    }

    public NonScrollableVP(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    @Override public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }
    @Override public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages return false;
        return false;
    }
}
