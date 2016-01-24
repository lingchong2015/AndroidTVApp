package com.dirk41.androidtvapp;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.app.BackgroundManager;
import android.util.DisplayMetrics;

/**
 * Created by lingchong on 16-1-23.
 */
public class SimpleBackgroundManager {
    private Activity mActivity;
    private static Drawable sDrawbleDefaultBackground;
    private BackgroundManager mBackgroundManager;

    private final int DEFAULT_BACKGROUND_RES_ID = R.drawable.default_background;
    private static final String TAG = SimpleBackgroundManager.class.getSimpleName();

    public SimpleBackgroundManager(Activity activity) {
        mActivity = activity;
        sDrawbleDefaultBackground = mActivity.getDrawable(DEFAULT_BACKGROUND_RES_ID);
        mBackgroundManager = BackgroundManager.getInstance(mActivity);
        mBackgroundManager.attach(mActivity.getWindow());
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
    }

    public void updateBackground(Drawable drawable) {
        mBackgroundManager.setDrawable(drawable);
    }

    public void clearBackground() {
        mBackgroundManager.setDrawable(sDrawbleDefaultBackground);
    }
}
