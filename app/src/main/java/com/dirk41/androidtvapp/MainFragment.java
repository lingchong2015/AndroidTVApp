package com.dirk41.androidtvapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lingchong on 16-1-19.
 */
public class MainFragment extends BrowseFragment {
    private ArrayObjectAdapter mArrayObjectAdapterRows;
    private static SimpleBackgroundManager sSimpleBackgroundManager;
    private static PicassoBackgroundManager sPicassoBackgroundManager;

    private static final int GRID_ITEM_WIDTH = 300;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final String TAG = MainFragment.class.getSimpleName();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        sSimpleBackgroundManager = new SimpleBackgroundManager(this.getActivity());
        sPicassoBackgroundManager = new PicassoBackgroundManager(this.getActivity());
        sPicassoBackgroundManager.clearBackground();

        setupUIElements();
        loadRows();
        setupEventListeners();
    }

    private void setupUIElements() {
//        this.setBadgeDrawable(this.getActivity().getResources().getDrawable(R.drawable.videos_by_google_banner));
        this.setTitle("你好，Android TV！");

        this.setHeadersState(HEADERS_ENABLED);
        this.setHeadersTransitionOnBackEnabled(true);

        //设置headers颜色;
        this.setBrandColor(this.getResources().getColor(R.color.fastlane_background));
        //设置搜索图标颜色;
        this.setSearchAffordanceColor(this.getResources().getColor(R.color.search_opaque));
    }

    private void loadRows() {
        mArrayObjectAdapterRows = new ArrayObjectAdapter(new ListRowPresenter());

        //Presenter;
        HeaderItem headerItemRow = new HeaderItem(0, "首部1");

        ArrayObjectAdapter arrayObjectAdapterRow = new ArrayObjectAdapter(new GridItemPresenter());
        arrayObjectAdapterRow.add("项目1");
        arrayObjectAdapterRow.add("项目2");
        arrayObjectAdapterRow.add("项目3");
        arrayObjectAdapterRow.add(getResources().getString(R.string.error_test_item));

        mArrayObjectAdapterRows.add(new ListRow(headerItemRow, arrayObjectAdapterRow));

        //CardPresenter;
        HeaderItem headerItemRowCardPresenter = new HeaderItem(1, "首部2");

        ArrayObjectAdapter arrayObjectAdapterRowCarPresenter = new ArrayObjectAdapter(new CardPresenter());
        for (int i = 0; i < 10; ++i) {
            Movie movie = new Movie();
            movie.setTitle("标题" + String.valueOf(i));
            movie.setStudio("影片" + String.valueOf(i));
            movie.setCardImageUrl("http://www.hkstv.hk:8080/adver/picture/2014/5/0c64828b-8e37-4d11-8a58-880382981731.jpg");
            arrayObjectAdapterRowCarPresenter.add(movie);
        }
        mArrayObjectAdapterRows.add(new ListRow(headerItemRowCardPresenter, arrayObjectAdapterRowCarPresenter));

        this.setAdapter(mArrayObjectAdapterRows);
    }

    private void setupEventListeners() {
        this.setOnItemViewSelectedListener(new ItemViewSelectedListener());
        this.setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof String) {
//                sSimpleBackgroundManager.clearBackground();
                sPicassoBackgroundManager.clearBackground();
            } else if (item instanceof Movie) {
//                sSimpleBackgroundManager.updateBackground(MainFragment.this.getActivity().getDrawable(R.drawable.movie));
                sPicassoBackgroundManager.updateBackgroundWithDelay(((Movie)item).getCardImageUrl());
            }
        }
    }

    private class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Intent intent = new Intent(MainFragment.this.getActivity(), DetailsActivity.class);
                intent.putExtra(VideoDetailsFragment.MOVIE, movie);
                MainFragment.this.getActivity().startActivity(intent);
            } else if (item instanceof String) {
                if (item.equals(getResources().getString(R.string.error_test_item))) {
                    Intent intent = new Intent(getActivity(), ErrorActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    //Presenter代表了CardInfo对象;
    private class GridItemPresenter extends Presenter {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(MainFragment.this.getActivity().getResources().getColor(R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {

        }
    }
}
