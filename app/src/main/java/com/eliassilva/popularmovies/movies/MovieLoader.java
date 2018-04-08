package com.eliassilva.popularmovies.movies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.eliassilva.popularmovies.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by Elias on 21/02/2018.
 */

/**
 * Perform async load of movie data
 */
public class MovieLoader extends android.support.v4.content.AsyncTaskLoader<List<MoviePOJO>> {
    private final String mSortBy;

    public MovieLoader(Context context, String sortBy) {
        super(context);
        mSortBy = sortBy;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MoviePOJO> loadInBackground() {
        if (mSortBy == null) {
            return null;
        }
        return NetworkUtils.extractMoviesFromJson(mSortBy);
    }
}
