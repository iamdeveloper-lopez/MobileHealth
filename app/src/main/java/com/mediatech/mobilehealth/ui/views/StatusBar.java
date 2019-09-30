package com.mediatech.mobilehealth.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.mediatech.mobilehealth.R;
import com.mediatech.mobilehealth.helper.AppHelper;

public class StatusBar extends FrameLayout {

    private Context context;
    private int statusBarColor = Integer.MIN_VALUE;

    public StatusBar(Context context) {
        super(context);
    }

    public StatusBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StatusBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public StatusBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        this.context = context;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.StatusBar, 0, 0);
            try {
                //with the text and colors specified using the names in attrs.xml
                statusBarColor = a.getInteger(R.styleable.StatusBar_statusBarColor, Integer.MIN_VALUE);//0 is default
            } finally {
                a.recycle();
            }
        }
    }

    public void setStatusBarColor(@ColorInt int color) {
        this.statusBarColor = color;
        requestLayout();
    }

    private Activity getActivity(Context context) {
        if (context == null)
            return null;
        else if (context instanceof Activity)
            return (Activity) context;
        else if (context instanceof ContextWrapper)
            return getActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        Activity host = getActivity(getContext());

        int height = AppHelper.getStatusBarHeight(host);
        Log.d("BAR", "onAttachedToWindow: " + height);
        ViewGroup.LayoutParams viewParams = getLayoutParams();
        if (viewParams != null) {
            viewParams.height = height;
        } else {
            viewParams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        }
        setLayoutParams(viewParams);

        if (statusBarColor == Integer.MIN_VALUE) {
            TypedValue typedValue = new TypedValue();
            host.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            statusBarColor = typedValue.data;
        }

        setBackgroundColor(statusBarColor);

    }

}
