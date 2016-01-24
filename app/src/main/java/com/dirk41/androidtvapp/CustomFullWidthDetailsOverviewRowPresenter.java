package com.dirk41.androidtvapp;

import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowPresenter;

/**
 * Created by lingchong on 16-1-23.<br/>
 * CustomFullWidthDetailsOverviewRowPresenter类继承于FullWidthDetailsOverviewRowPresenter类。<br/>
 * FullWidthDetailsOverviewRowPresenter类用于渲染DetailsOverviewRow来显示一个条目。
 */
public class CustomFullWidthDetailsOverviewRowPresenter extends FullWidthDetailsOverviewRowPresenter {
    private static final String TAG = CustomFullWidthDetailsOverviewRowPresenter.class.getSimpleName();

    CustomFullWidthDetailsOverviewRowPresenter(Presenter presenter) {
        super(presenter);
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);
        this.setState((ViewHolder) holder, FullWidthDetailsOverviewRowPresenter.STATE_SMALL);
    }
}
