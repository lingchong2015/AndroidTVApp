package com.dirk41.androidtvapp;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

/**
 * Created by lingchong on 16-1-23.
 */
public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder vh, Object item) {
        Movie movie = (Movie) item;

        if (movie != null) {
            vh.getTitle().setText(movie.getTitle());
            vh.getSubtitle().setText(movie.getStudio());
            vh.getBody().setText(movie.getDescription());
        }
    }
}
