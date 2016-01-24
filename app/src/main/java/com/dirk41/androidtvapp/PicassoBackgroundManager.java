package com.dirk41.androidtvapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v17.leanback.app.BackgroundManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lingchong on 16-1-23.
 */
public class PicassoBackgroundManager {
    private static Drawable sDrawableDefaultBackgroud;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Activity mActivity;
    private BackgroundManager mBackgroundManager = null;
    private DisplayMetrics mDisplayMetrics;
    private URI mBackgroundURI;
    private PicassoBackgroundManagerTarget mPicassoBackgroundManagerTarget;
    private Timer mTimer;

    private static final int DEFUALT_BACKGROUND_RES_ID = R.drawable.default_background;
    private static final int BACKGROUND_UPDATE_DELAY = 500;
    private static final String TAG = PicassoBackgroundManager.class.getSimpleName();

    public PicassoBackgroundManager(Activity activity) {
        mActivity = activity;
        sDrawableDefaultBackgroud = mActivity.getDrawable(DEFUALT_BACKGROUND_RES_ID);
        mBackgroundManager = BackgroundManager.getInstance(mActivity);
        mBackgroundManager.attach(mActivity.getWindow());
        mPicassoBackgroundManagerTarget = new PicassoBackgroundManagerTarget(mBackgroundManager);
        mDisplayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    public void updateBackgroundWithDelay(String url) {
        try {
            URI uri = new URI(url);
            updateBackgroundWithDelay(uri);
        } catch (URISyntaxException e) {
//            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    public void updateBackgroundWithDelay(URI uri) {
        mBackgroundURI = uri;
        startBackgroundTimer();
    }

    public void clearBackground() {
        try {
            Picasso.with(mActivity)
                    .load(R.drawable.movie)
                    .resize(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels)
                    .centerCrop()
                    .error(sDrawableDefaultBackgroud)
                    .into(mPicassoBackgroundManagerTarget);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void updateBackground(URI uri) {
        try {
            Picasso.with(mActivity)
                    .load(uri.toString())
                    .resize(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels)
                    .centerCrop()
                    .error(sDrawableDefaultBackgroud)
                    .into(mPicassoBackgroundManagerTarget);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void startBackgroundTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new Timer();
        mTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private class UpdateBackgroundTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mBackgroundURI != null) {
                        updateBackground(mBackgroundURI);
                    }
                }
            });
        }
    }

    public class PicassoBackgroundManagerTarget implements Target {
        private BackgroundManager mBackgroundManager;

        public PicassoBackgroundManagerTarget(BackgroundManager backgroundManager) {
            mBackgroundManager = backgroundManager;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mBackgroundManager.setBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            mBackgroundManager.setDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }

            PicassoBackgroundManagerTarget that = (PicassoBackgroundManagerTarget)o;
            if (!mBackgroundManager.equals(that.mBackgroundManager)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return mBackgroundManager.hashCode();
        }
    }
}
