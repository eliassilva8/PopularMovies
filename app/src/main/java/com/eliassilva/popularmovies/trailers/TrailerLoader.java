package com.eliassilva.popularmovies.trailers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.eliassilva.popularmovies.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by Elias on 28/02/2018.
 */

/**
 * Perform async load of trailer data
 */
public class TrailerLoader extends AsyncTaskLoader<List<TrailerPOJO>> {
    private final int mMovieId;

    public TrailerLoader(Context context, int movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<TrailerPOJO> loadInBackground() {
        if (mMovieId == 0) {
            throw new RuntimeException("Movie ID equal to null");
        }
        return NetworkUtils.extractTrailersFromJson(mMovieId);
    }
}
