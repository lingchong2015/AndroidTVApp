package com.dirk41.androidtvapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by lingchong on 16-1-23.
 */

//DetailsFragment用于创建Leanback详细内容屏幕，DetailsFragment负责渲染其ObjectAdapter对象中的所有元素，这些元素被视为垂直列表中的行。
//所有ObjectAdapter中的元素都必须是Row类的子类，ObjectAdapter的PresenterSelector类必须存放RowPresenter类型的子类。
public class VideoDetailsFragment extends DetailsFragment {
    private CustomFullWidthDetailsOverviewRowPresenter mCustomFullWidthDetailsOverviewRowPresenter;
    private PicassoBackgroundManager mPicassoBackgroundManager;
    private Movie mSelectedMovie;
    private DetailsRowBuilderTask mDetailsRowBuilderTask;

    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;
    public static final String MOVIE = "com.dirk41.Movie";
    private static final String TAG = VideoDetailsFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化FullWidthDetailsOverviewRowPresenter类，将其customizable detailed description view的Presenter设置为DetailDescriptionPresenter类;
        mCustomFullWidthDetailsOverviewRowPresenter = new CustomFullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        mPicassoBackgroundManager = new PicassoBackgroundManager(this.getActivity());
        mSelectedMovie = this.getActivity().getIntent().getParcelableExtra(MOVIE);

        mDetailsRowBuilderTask = (DetailsRowBuilderTask) new DetailsRowBuilderTask().execute(mSelectedMovie);
        mPicassoBackgroundManager.updateBackgroundWithDelay(mSelectedMovie.getCardImageUrl());
    }

    @Override
    public void onStop() {
        super.onStop();
        mDetailsRowBuilderTask.cancel(true);
    }

    private class DetailsRowBuilderTask extends AsyncTask<Movie, Integer, DetailsOverviewRow> {
        @Override
        protected DetailsOverviewRow doInBackground(Movie... params) {
            DetailsOverviewRow detailsOverviewRow = new DetailsOverviewRow(params[0]);
            try {
                Bitmap poster = Picasso.with(VideoDetailsFragment.this.getActivity())
                        .load(mSelectedMovie.getCardImageUrl())
                        .resize(Utils.convertDpToPixel(VideoDetailsFragment.this.getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH),
                                Utils.convertDpToPixel(VideoDetailsFragment.this.getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT))
                        .centerCrop()
                        .get();
                //设置DetailsOverview的Logo;
                detailsOverviewRow.setImageBitmap(VideoDetailsFragment.this.getActivity(), poster);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }

            return detailsOverviewRow;
        }

        @Override
        protected void onPostExecute(DetailsOverviewRow detailsOverviewRow) {
            SparseArrayObjectAdapter sparseArrayObjectAdapter = new SparseArrayObjectAdapter();
            for (int i = 0; i < 10; ++i) {
                Action action = new Action(i, "lable1", "lable2");
                sparseArrayObjectAdapter.set(i, new Action(i, "lable1", "lable2"));
            }
            detailsOverviewRow.setActionsAdapter(sparseArrayObjectAdapter);

            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
            for (int i = 0; i < 10; ++i) {
                Movie movie = new Movie();
                if (i % 3 == 0) {
                    movie.setCardImageUrl("http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02580.jpg");
                } else if (i % 3 == 1) {
                    movie.setCardImageUrl("http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02630.jpg");
                } else {
                    movie.setCardImageUrl("http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02529.jpg");
                }
                movie.setTitle("title" + i);
                movie.setStudio("studio" + i);
                listRowAdapter.add(movie);
            }
            HeaderItem headerItem = new HeaderItem(0, "相关视频");
            ListRow listRow = new ListRow(headerItem, listRowAdapter);

            ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
            mCustomFullWidthDetailsOverviewRowPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_SMALL);
            classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, mCustomFullWidthDetailsOverviewRowPresenter);
            classPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
            ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(classPresenterSelector);

            arrayObjectAdapter.add(detailsOverviewRow);
            arrayObjectAdapter.add(listRow);

            VideoDetailsFragment.this.setAdapter(arrayObjectAdapter);
        }
    }
}
